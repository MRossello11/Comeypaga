package feature_user.presentation

import core.Constants
import core.Properties
import core.model.Restaurant
import feature_user.domain.model.Order
import feature_user.domain.model.OrderLine
import feature_user.domain.use_cases.UserOrderUseCases
import feature_user.presentation.UserOrderEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserOrderController(
    private val userOrderUseCases: UserOrderUseCases,
    order: Order? = null
) {
    private val _state = MutableStateFlow(UserOrderState(
        Order(
            userId = Properties.userLogged?._id!!,
            shippingAddress = Properties.userLogged?.address!!
        )
    ))
    val state: StateFlow<UserOrderState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        // if an order is passed, use it
        order?.let {
            _state.update { state ->
                state.copy(
                    order = it.copy(
                        userId = Properties.userLogged?._id!!,
                        shippingAddress = Properties.userLogged?.address!!
                    )
                )
            }
        } ?: run {
            // if no order is passed, get the orders from database
            // if there is no created order, it will be created later
            getOrdersInCurse()
        }
    }

    private fun getOrdersInCurse() = CoroutineScope(Dispatchers.IO).launch {
            // recover orders (that are not sent) from user
            userOrderUseCases.getOrdersUser(
                userId = Properties.userLogged?._id!!,
                callback = { response, orders ->
                    // differ orders in curse from created order
                    var order = Order(
                        userId = Properties.userLogged?._id!!,
                        shippingAddress = Properties.userLogged?.address!!
                    )
                    val ordersInCurse = arrayListOf<Order>()
                    orders.forEach {
                        if (it.state == Constants.OrderStates.CREATED) {
                            order = it
                        } else {
                            ordersInCurse.add(it)
                        }
                    }

                    _state.update { state ->
                        state.copy(
                            order = order,
                            ordersInCurse = ordersInCurse,
                            response = response
                        )
                    }
                }
            )
        }

    // setup order lines (creates 1 orderLine per plate)
    fun setOrderInCurse(restaurant: Restaurant) {
        // lines of current order
        val orderLines = arrayListOf<OrderLine>()
        // create order lines
        restaurant.menu?.forEach { plate ->
            // get the orderLine for this plate if already exists
            val matchLine = _state.value.order.orderLines.find { plate._id == it.plateId }

            orderLines.add(
                OrderLine(
                    plateId = plate._id,
                    plateName = plate.plateName,
                    // if line already exists, use its quantity (the rest of data is the same as plate)
                    quantity = matchLine?.quantity ?: 0,
                    price = plate.price.toFloatOrNull() ?: 0f
                )
            )
        }
        _state.update { state ->
            state.copy(
                order = state.order.copy(
                    orderLines = orderLines
                )
            )
        }
    }

    fun onEvent(event: UserOrderEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            when (event) {
                is UpdateOrder -> {
                    // if a restaurant is passed, the order is being created, if not, the order
                    // is being modified (from cart or order detail)
                    event.restaurant?.let { restaurant ->
                        // if there's an order open, and it's not from the same restaurant
                        if (
                            _state.value.order.restaurantId.isNotEmpty() &&
                            _state.value.order.restaurantId != (restaurant._id ?: "")
                        ) {
                            _eventFlow.emit(UiEvent.ShowDialog("Only one order per restaurant is permitted"))
                            return@launch
                        }

                        // set restaurant if it's not set already
                        if (_state.value.order.restaurantId.isEmpty()) {
                            restaurant.let {
                                _state.update { state ->
                                    state.copy(
                                        order = state.order.copy(
                                            restaurantId = it._id!!,
                                            restaurantName = it.name
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // update orderLines

                    // get current lines
                    val newOrderLines = arrayListOf<OrderLine>()
                    newOrderLines.addAll(_state.value.order.orderLines)
                    // find match
                    val match = newOrderLines.find { it.plateId == event.newOrderLine.plateId }
                    match?.let {
                        // replace old order line
                        val index = newOrderLines.indexOf(it)
                        newOrderLines[index] = event.newOrderLine
                        _state.update { state ->
                            state.copy(
                                order = state.order.copy(
                                    orderLines = newOrderLines
                                )
                            )
                        }
                    }

                    // update order with orderLines w/quantity > 0
                    val orderLinesToSend = arrayListOf<OrderLine>()
                    orderLinesToSend.addAll(_state.value.order.orderLines)

                    orderLinesToSend.removeIf { it.quantity <= 0 }

                    userOrderUseCases.updateOrder(
                        order = _state.value.order.copy(
                            orderLines = orderLinesToSend
                        ),
                        callback = { response, order ->
                            _state.update { state ->
                                state.copy(
                                    response = response
                                )
                            }
                            order?.let {
                                _state.update { state ->
                                    state.copy(
                                        order = state.order.copy(
                                            _id = it._id ?: state.order._id,
                                            state = it.state
                                        )
                                    )
                                }
                            }
                        }
                    )

                    // handle response
                    if (_state.value.response.errorCode !in 200..299){
                        _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error ocurred"))
                    }
                }

                CancelOrder -> {
                    userOrderUseCases.cancelOrder(
                        order = _state.value.order,
                        callback = { response ->
                            _state.update { state ->
                                state.copy(
                                    response = response
                                )
                            }
                        }
                    )
                    // handle response
                    if (_state.value.response.errorCode in 200..299){
                        _state.update { state ->
                            state.copy(
                                order = state.order.copy(
                                    state = Constants.OrderStates.CANCELED
                                )
                            )
                        }
                        _eventFlow.emit(UiEvent.ShowDialog("Order canceled"))
                    } else {
                        _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error ocurred"))
                    }
                }

                // 'send' the order to restaurant (change state from 'created' to 'in progress')
                SendOrder -> {
                    _state.update { state ->
                        state.copy(
                            order = _state.value.order.copy(state = Constants.OrderStates.IN_PROGRESS)
                        )
                    }

                    userOrderUseCases.updateOrder(
                        order = _state.value.order,
                        callback = { response, order ->
                            _state.update { state ->
                                state.copy(
                                    response = response
                                )
                            }
                            // if order was sent (user sent it to be in progress)
                            order?.let {
                                _state.update { state ->
                                    state.copy(
                                        order = order
                                    )
                                }
                            }
                        }
                    )
                    // handle response
                    if (_state.value.response.errorCode in 200..299){
                        _eventFlow.emit(UiEvent.ShowDialog("Order sent"))
                    } else {
                        _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error ocurred"))
                    }
                }
            }

        }
    }

    sealed class UiEvent {
        data class ShowDialog(val message: String): UiEvent()
    }
}