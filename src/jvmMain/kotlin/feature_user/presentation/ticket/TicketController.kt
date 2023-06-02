package feature_user.presentation.ticket

import feature_user.domain.model.Order
import feature_user.domain.use_cases.GetRestaurantData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TicketController(
    private val getRestaurantData: GetRestaurantData,
    private val order: Order
) {

    private val _state = MutableStateFlow(TicketState(
        order = order
    ))
    val state: StateFlow<TicketState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getRestaurantData(
                restaurantId = order.restaurantId,
                callback = { response, restaurant ->
                    _state.update { state ->
                        state.copy(
                            response = response
                        )
                    }

                    if (_state.value.response.errorCode in 200..299){
                        restaurant?.let {
                            _state.update { state ->
                                state.copy(
                                    restaurant = it
                                )
                            }
                        }
                    } else {
                        launch {
                            _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error occurred retrieving restaurant data"))
                        }
                    }
                }
            )
        }
    }

    sealed class UiEvent {
        data class ShowDialog(val message: String): UiEvent()
    }
}