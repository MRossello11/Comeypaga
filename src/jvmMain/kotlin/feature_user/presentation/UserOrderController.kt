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
    private val userOrderUseCases: UserOrderUseCases
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
        getOrdersInCurse()
    }

    private fun getOrdersInCurse() = CoroutineScope(Dispatchers.IO).launch {
            // recover orders (that are not sent) from user
            userOrderUseCases.getOrdersUser(
                userId = Properties.userLogged?._id!!,
                callback = { response, orders ->
                    // differ orders in curse from created order
                    var order = Order(userId = Properties.userLogged?._id!!)
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

    // setup orderlines (creates 1 orderLine per plate)
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
                    // if there's an order open, and it's not from the same restaurant
                    if (
                        _state.value.order.restaurantId.isNotEmpty() &&
                        _state.value.order.restaurantId != (event.restaurant?._id ?: "")
                    ){
                        _eventFlow.emit(UiEvent.ShowDialog("Only one order per restaurant is permitted"))
                        return@launch
                    }

                    // set restaurant if it's not set already
                    if (_state.value.order.restaurantId.isEmpty()){
                        event.restaurant?.let {
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
                                    response = response,
                                    order = state.order.copy(
                                        _id = order?._id ?: state.order._id
                                    )
                                )
                            }
                        }
                    )

                    // handle response
                    if (_state.value.response.errorCode !in 200..299){
                        _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error ocurred"))
                    }
                }

                is CancelOrder -> {
                    userOrderUseCases.cancelOrder(
                        order = event.order,
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
                        _eventFlow.emit(UiEvent.ShowDialog("Order canceled"))
                    } else {
                        _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error ocurred"))
                    }
                }

                // 'send' the order to restaurant (change state from 'created' to 'in progress')
                is SendOrder -> {
                    _state.update { state ->
                        state.copy(
                            order = event.order.copy(state = Constants.OrderStates.IN_PROGRESS)
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