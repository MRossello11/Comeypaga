package feature_admin.presentation.add_modify_plate

import feature_admin.domain.model.InvalidPlateRequest
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.use_cases.AdminUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddModifyPlateController (
    private val adminUseCases: AdminUseCases
){

    private val _state = MutableStateFlow(AddModifyPlateState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddModifyPlateEvent){
        when(event){
            is AddModifyPlateEvent.FieldEntered -> {
                _state.update { state ->
                    state.copy(
                        plate = event.plate
                    )
                }
            }
            AddModifyPlateEvent.CreatePlate -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        adminUseCases.addPlate(
                            plateRequest = PlateRequest(
                                restaurantId = _state.value.restaurant?._id!!,
                                plateId = null,
                                plateName = _state.value.plate?.plateName,
                                description = _state.value.plate?.description,
                                price = _state.value.plate?.price,
                                type = _state.value.plate?.type,
                            ),
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        response = it
                                    )
                                }
                            },
                            newPlate = true
                        )
                        // handle response
                        if (_state.value.response.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.PlateCreated)
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.response.message ?: "An error occurred"))
                        }
                    } catch (ipr: InvalidPlateRequest){
                        // handle error
                        _eventFlow.emit(UiEvent.ShowDialog(ipr.message ?: "An error occurred"))
                    }
                }
            }
            AddModifyPlateEvent.ModifyPlate -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        adminUseCases.addPlate(
                            plateRequest = PlateRequest(
                                restaurantId = _state.value.restaurant?._id!!,
                                plateName = _state.value.plate?.plateName!!,
                                description = _state.value.plate?.description!!,
                                price = _state.value.plate?.price!!,
                                type = _state.value.plate?.type!!
                            ),
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        response = it
                                    )
                                }
                            },
                            newPlate = false
                        )
                        // handle response
                        if (_state.value.response.errorCode in 200..299) {
                            _eventFlow.emit(UiEvent.PlateCreated)
                        } else {
                            _eventFlow.emit(
                                UiEvent.ShowDialog(
                                    message = _state.value.response.message ?: "An error occurred"
                                )
                            )
                        }
                    } catch (ipr: InvalidPlateRequest) {
                        // handle error
                        _eventFlow.emit(UiEvent.ShowDialog(ipr.message ?: "An error occurred"))
                    }
                }
            }
        }
    }

    // todo
    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()

        object PlateCreated: UiEvent()

    }
}