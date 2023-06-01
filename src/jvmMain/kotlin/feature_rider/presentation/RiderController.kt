package feature_rider.presentation

import core.Properties
import feature_rider.domain.model.UpdateOrderStateRiderRequest
import feature_rider.domain.use_cases.RiderUseCases
import feature_user.domain.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RiderController(
    private val riderUseCases: RiderUseCases
) {

    private val _state = MutableStateFlow(RiderState(
        listOf(),
        listOf()
    ))
    val state: StateFlow<RiderState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            // get orders
            riderUseCases.getOrders(
                riderId = Properties.userLogged?._id!!,
                callback = { response, allOrders, ordersRider ->
                    _state.update { state ->
                        state.copy(
                            response = response,
                            allOrders = allOrders,
                            riderOrders = ordersRider
                        )
                    }
                }
            )
            // handle response
            if (_state.value.response.errorCode !in 200..299){
                _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error occurred updating getting the orders"))
            }
        }
    }

    fun updateOrder(orderId: String, newState: Int){
        CoroutineScope(Dispatchers.IO).launch {
            riderUseCases.updateOrderState(
                updateOrderStateRiderRequest = UpdateOrderStateRiderRequest(
                    _id = orderId,
                    newState = newState,
                    riderId = Properties.userLogged?._id!!
                ),
                callback = { response ->
                    val all = arrayListOf<Order>()
                    val rider = arrayListOf<Order>()
                    all.addAll(_state.value.allOrders)
                    rider.addAll(_state.value.riderOrders)

                    _state.update { state ->
                        state.copy(
                            response = response,
                            // force ui update
                            allOrders = all,
                            riderOrders = rider
                        )
                    }
                }
            )
            // handle response
            if (_state.value.response.errorCode in 200..299){
                _eventFlow.emit(UiEvent.ShowDialog("Order updated"))
            } else {
                _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "An error occurred updating the order"))
            }
        }
    }
    sealed class UiEvent {
        data class ShowDialog(val message: String): UiEvent()
    }
}