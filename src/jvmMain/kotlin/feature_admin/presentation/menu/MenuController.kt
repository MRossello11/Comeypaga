package feature_admin.presentation.menu

import core.model.InvalidRestaurant
import core.model.Restaurant
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.use_cases.AdminUseCases
import feature_admin.presentation.menu.MenuEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuController(
    private val adminUseCases: AdminUseCases,
    private val restaurant: Restaurant
) {
    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init{
        getRestaurant()
    }

    fun onEvent(event: MenuEvent){
        when(event){
            is DeletePlate -> {
                _state.update { state ->
                    state.copy(
                        actualPlate = event.plate
                    )
                }
                CoroutineScope(Dispatchers.IO).launch {
                    _eventFlow.emit(UiEvent.ShowDeletePlateDialogConfirmation("Confirm deletion of '${_state.value.actualPlate!!.plateName}'"))
                }
            }
            is DeletionConfirmed -> {
                // delete plate
                CoroutineScope(Dispatchers.IO).launch{
                    adminUseCases.deletePlate(
                        callback = { response ->
                            _state.update { state ->
                                state.copy(
                                    response = response
                                )
                            }
                        },
                        plateRequest = PlateRequest(
                            restaurantId = _state.value.restaurant?._id!!,
                            plateId = _state.value.actualPlate?._id!!
                        )
                    )

                    getRestaurant()

                    if (_state.value.response.errorCode in 200..299){
                        _eventFlow.emit(UiEvent.ShowDialog("Plate deleted"))
                    } else {
                        _eventFlow.emit(UiEvent.ShowDialog("Error deleting plate"))
                    }
                }
            }

            is SetPlates -> {
                _state.update { state ->
                    state.copy(
                        restaurant = state.restaurant?.copy(
                            menu = event.plates
                        )
                    )
                }
            }

            is SetPlate -> {
                _state.update { state ->
                    state.copy(
                        actualPlate = event.plate
                    )
                }
            }
        }
    }

    private fun getRestaurant() {
        _state.update { state ->
            state.copy(
                restaurant = restaurant
            )
        }
        // get restaurant
        CoroutineScope(Dispatchers.IO).launch{
            try {
                adminUseCases.getRestaurant(
                    restaurant = _state.value.restaurant!!,
                    callback = { response, restaurant ->
                        _state.update { state ->
                            state.copy(
                                response = response,
                                restaurant = restaurant
                            )
                        }
                    }
                )
            } catch (ir: InvalidRestaurant){
                _eventFlow.emit(UiEvent.ShowDialog(ir.message ?: "An error occurred"))
            }
        }
    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
        data class ShowDeletePlateDialogConfirmation(val message: String): UiEvent()
    }
}