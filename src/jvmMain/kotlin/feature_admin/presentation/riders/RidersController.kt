package feature_admin.presentation.riders

import feature_admin.domain.use_cases.AdminUseCases
import feature_users.domain.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RidersController(
    private val adminUseCases: AdminUseCases
) {
    private val _state = MutableStateFlow(RidersState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RidersEvent){
        when(event){
            is RidersEvent.DeleteRider -> {
                _state.update { state ->
                    state.copy(
                        actualRider = event.rider
                    )
                }

                // show dialog confirmation
                CoroutineScope(Dispatchers.IO).launch {
                    _eventFlow.emit(UiEvent.ShowDeleteRiderDialogConfirmation("Confirm deletion of rider '${event.rider.username}'?"))
                }
            }

            RidersEvent.DeletionConfirmed -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // delete rider
                        adminUseCases.deleteRider(
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        response = it
                                    )
                                }
                            },
                            riderId = _state.value.actualRider!!._id
                        )

                        getRiders()

                        // evaluate response
                        if (_state.value.response.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.ShowDialog("Rider deleted"))
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog("Error deleting rider"))
                        }
                    } catch (e: Exception){
                        _eventFlow.emit(UiEvent.ShowDialog("An error occurred deleting the rider"))
                    }
                }
            }
        }
    }

    suspend fun getRiders(): List<User>?{
        try{
            adminUseCases.getRiders(
                callback = { response, riders ->
                    _state.update { state ->
                        state.copy(
                            riders = riders,
                            response = response
                        )
                    }
                }
            )
        } catch (e: Exception) {
            _eventFlow.emit(UiEvent.ShowDialog("Error getting riders"))
        }
        if (_state.value.response.errorCode in 200..299){
            return _state.value.riders
        } else {
            _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "Error getting riders"))
        }
        return null
    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
        data class ShowDeleteRiderDialogConfirmation(val message: String): UiEvent()
    }
}