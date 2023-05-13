package feature_admin.presentation.menu

import feature_admin.domain.model.PlateRequest
import feature_admin.domain.use_cases.AdminUseCases
import feature_admin.presentation.menu.MenuEvent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MenuController(
    private val adminUseCases: AdminUseCases
) {
    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: MenuEvent){
        when(event){
            is DeletePlate -> {
                _state.update { state ->
                    state.copy(
                        actualPlate = event.plate
                    )
                }
                CoroutineScope(Dispatchers.IO).launch {
                    _eventFlow.emit(UiEvent.ShowDeletePlateDialogConfirmation("Confirm deletion of ${_state.value.actualPlate!!.plateName}"))
                }
            }
            is DeletionConfirmed -> {
                _state.update { state ->
                    val newMenu = state.restaurant?.menu?.toMutableList() ?: mutableListOf()

                    newMenu.remove(state.actualPlate)

                    state.copy(
                        actualPlate = null,
                        restaurant = state.restaurant?.copy(
                            menu = newMenu
                        )
                    )
                }

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

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
        data class ShowDeletePlateDialogConfirmation(val message: String): UiEvent()
    }
}