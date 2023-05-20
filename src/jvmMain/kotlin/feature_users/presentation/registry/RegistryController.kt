package feature_users.presentation.registry

import feature_admin.domain.use_cases.PostRider
import feature_users.domain.model.InvalidUser
import feature_users.domain.model.Role
import feature_users.domain.model.UserResponse
import feature_users.domain.use_cases.UserUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistryController(
    private val userUseCases: UserUseCases,
    private val userRole: Role,
    private val user: UserResponse = UserResponse(),
    private val postRider: PostRider // todo: temp
) {
    private val _state = MutableStateFlow(RegistryState(
        user = user,
    ))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegistryEvent){
        when(event){
            is RegistryEvent.FieldEntered -> {
                _state.update { state ->
                    state.copy(
                        user = event.user
                    )
                }
            }
            RegistryEvent.Registry -> {
                // handle registry
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        // check that passwords match
                        if (_state.value.user.password != _state.value.user.passwordConfirmation){
                            throw InvalidUser("Passwords do not match")
                        }

                        userUseCases.registryUseCase(
                            userRegistryRequest = user.copy(role = userRole.toString()),
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        registryResponse = it
                                    )
                                }
                            }
                        )

                        // handle response
                        if (_state.value.registryResponse.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.Registry)
                        } else{
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.registryResponse.message ?: "Error registering user"))

                        }
                    } catch (iu: InvalidUser){
                        _eventFlow.emit(UiEvent.ShowDialog(message = iu.message ?: "Error registering user"))
                    }
                }
            }

            RegistryEvent.Modify -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try{
                        postRider(
                            rider = _state.value.user,
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        registryResponse = it
                                    )
                                }
                            }
                        )

                        // handle response
                        if (_state.value.registryResponse.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.ShowDialog(message = "User modified"))
                        } else{
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.registryResponse.message ?: "Error modifying user"))
                        }
                    } catch (iu: InvalidUser){
                        _eventFlow.emit(UiEvent.ShowDialog(message = iu.message ?: "Error modifying user"))
                    }
                }
            }
        }
    }
    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
        object Registry: UiEvent()
    }
}