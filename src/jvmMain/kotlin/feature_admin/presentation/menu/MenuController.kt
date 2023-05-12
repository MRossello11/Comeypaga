package feature_admin.presentation.menu

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
                    _eventFlow.emit(UiEvent.ShowDeletePlateDialogConfirmation("Confirm deletion of ${_state.value.actualPlate!!.name}"))
                }
            }
            is ConfirmDelete -> {
                _state.update { state ->
                    val newMenu = state.menu.toMutableList()

                    newMenu.remove(state.actualPlate)

                    state.copy(
                        actualPlate = null,
                        menu = newMenu
                    )
                }
            }

            is SetPlates -> {
                _state.update { state ->
                    state.copy(
                        menu = event.plates
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