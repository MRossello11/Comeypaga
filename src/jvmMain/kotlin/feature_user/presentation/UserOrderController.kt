package feature_user.presentation

import core.Properties
import core.model.Restaurant
import feature_user.domain.model.Order
import feature_user.domain.use_cases.UserOrderUseCases
import feature_user.presentation.UserOrderEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserOrderController(
    restaurant: Restaurant,
    private val userOrderUseCases: UserOrderUseCases
) {
    private val _state = MutableStateFlow(UserOrderState(
        Order(
            restaurantId = restaurant._id!!,
            restaurantName = restaurant.name,
            userId = Properties.userLogged?._id!!,
            shippingAddress = Properties.userLogged?.address!!
        )
    ))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: UserOrderEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            when (event) {
                is UpdateOrder -> {
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

    sealed class UiEvent {
        data class ShowDialog(val message: String): UiEvent()

    }
}