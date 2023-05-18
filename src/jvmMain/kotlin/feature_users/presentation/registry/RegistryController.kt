package feature_users.presentation.registry

import core.model.Address
import feature_users.domain.model.InvalidUser
import feature_users.domain.model.UserResponse
import feature_users.domain.use_cases.UserUseCases
import feature_users.presentation.registry.Field.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegistryController(
    private val userUseCases: UserUseCases
) {
    private val _registryState = MutableStateFlow(RegistryState())
    val registryState = _registryState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegistryEvent){
        when(event){
            is RegistryEvent.FieldEntered -> {
                when(event.fieldEntered){
                    USERNAME -> {
                        _registryState.update { state ->
                            state.copy(
                                username = event.value
                            )
                        }
                    }
                    FIRSTNAME -> {
                        _registryState.update { state ->
                            state.copy(
                                firstname = event.value
                            )
                        }
                    }
                    LASTNAME -> {
                        _registryState.update { state ->
                            state.copy(
                                lastname = event.value
                            )
                        }
                    }
                    BIRTH_DATE -> {
                        _registryState.update { state ->
                            state.copy(
                                birthDate = event.value
                            )
                        }
                    }
                    PHONE -> {
                        _registryState.update { state ->
                            state.copy(
                                phone = event.value
                            )
                        }
                    }
                    EMAIL -> {
                        _registryState.update { state ->
                            state.copy(
                                email = event.value
                            )
                        }
                    }
                    STREET -> {
                        _registryState.update { state ->
                            state.copy(
                                street = event.value
                            )
                        }
                    }
                    TOWN -> {
                        _registryState.update { state ->
                            state.copy(
                                town = event.value
                            )
                        }
                    }
                    PASSWORD -> {
                        _registryState.update { state ->
                            state.copy(
                                password = event.value
                            )
                        }
                    }
                    PASSWORD_CONFIRMATION -> {
                        _registryState.update { state ->
                            state.copy(
                                passwordConfirmation = event.value
                            )
                        }
                    }
                }
            }
            RegistryEvent.Registry -> {
                // handle registry
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        // check that passwords match
                        if (_registryState.value.password != _registryState.value.passwordConfirmation){
                            throw InvalidUser("Passwords do not match")
                        }

                        userUseCases.registryUseCase(
                            userRegistryRequest = UserResponse(
                                _id = "",
                                username = _registryState.value.username,
                                firstname = _registryState.value.firstname,
                                lastname = _registryState.value.lastname,
                                birthDate = _registryState.value.birthDate,
                                phone = _registryState.value.phone,
                                email = _registryState.value.email,
                                address = Address(
                                    street = _registryState.value.street,
                                    town = _registryState.value.town
                                ),
                                password = _registryState.value.password,
                            ),
                            callback = {
                                _registryState.update { state ->
                                    state.copy(
                                        registryResponse = it
                                    )
                                }
                            }
                        )

                        // handle response
                        if (_registryState.value.registryResponse.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.Registry)
                        } else{
                            _eventFlow.emit(UiEvent.ShowDialog(message = _registryState.value.registryResponse.message ?: "Error registering user"))

                        }
                    } catch (iu: InvalidUser){
                        _eventFlow.emit(UiEvent.ShowDialog(message = iu.message ?: "Error registering user"))
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