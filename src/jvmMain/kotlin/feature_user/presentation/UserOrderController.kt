package feature_user.presentation

import core.Constants
import core.Properties
import feature_user.domain.model.Order
import feature_user.domain.use_cases.UserOrderUseCases
import feature_user.presentation.UserOrderEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getOrdersInCurse()
    }

    private fun getOrdersInCurse() = CoroutineScope(Dispatchers.IO).async {
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

                    // add, modify or delete new order line
                    if (event.newOrderLine.quantity == 0){
                        // delete line if quantity == 0
                        val newOrderLines = _state.value.order.orderLines
                        newOrderLines.removeIf { it.plateId == event.newOrderLine.plateId }

                        // update value
                        _state.update { state ->
                            state.copy(
                                order = state.order.copy(
                                    orderLines = newOrderLines
                                )
                            )
                        }
                    } else {
                        // add or modify if quantity > 0
                        val indexOfNewLine = _state.value.order.orderLines.indexOf(event.newOrderLine)
                        val newOrderLines = _state.value.order.orderLines

                        if (indexOfNewLine == -1){
                            // if it's a new line, add it
                            newOrderLines.add(event.newOrderLine)
                        } else {
                            // if the line already exists, replace it
                            newOrderLines[indexOfNewLine] = event.newOrderLine
                        }
                        // update values
                        _state.update { state ->
                            state.copy(
                                order = state.order.copy(
                                    orderLines = newOrderLines
                                )
                            )
                        }
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

                is SendOrder -> {
                    _state.update { state ->
                        state.copy(
                            order = event.order
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

    suspend fun getCurrentOrder(): Order{
        getOrdersInCurse().await()
        return _state.value.order
    }

    sealed class UiEvent {
        data class ShowDialog(val message: String): UiEvent()
        data class UpdateUi(val order: Order): UiEvent()
    }
}