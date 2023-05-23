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
            userId = Properties.userLogged?._id!!
        )
    ))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: UserOrderEvent) {
        CoroutineScope(Dispatchers.IO).launch {
            when (event) {
                is UpdateOrder -> {
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