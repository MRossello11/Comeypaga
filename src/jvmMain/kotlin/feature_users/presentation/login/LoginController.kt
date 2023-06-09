package feature_users.presentation.login

import core.Properties
import feature_users.domain.model.InvalidLoginRequest
import feature_users.domain.model.LoginRequest
import feature_users.domain.use_cases.UserUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginController(
    private val userUseCases: UserUseCases
) {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when(event){
            is LoginEvent.PasswordEntered -> {
                _loginState.update { currentState ->
                    currentState.copy(
                        password = event.value
                    )
                }
            }
            is LoginEvent.UsernameEntered -> {
                _loginState.update { currentState ->
                    currentState.copy(
                        username = event.value
                    )
                }
            }

            is LoginEvent.Login -> {
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        userUseCases.loginUseCase(
                            loginRequest = LoginRequest(event.username, event.password),
                            callback = { user, response ->
                                _loginState.update { currentState ->
                                    currentState.copy(
                                        user = user,
                                        loginResponse = response
                                    )
                                }
                            }
                        )
                        if (_loginState.value.loginResponse.errorCode in 200..299){
                            _loginState.value.user?.let {
                                Properties.userLogged = it
                                _eventFlow.emit(UiEvent.Login)
                            } ?: run {
                                throw InvalidLoginRequest("Error logging in")
                            }
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(_loginState.value.loginResponse.message?.ifEmpty { "Error" } ?: "Error"))
                        }
                    } catch (ilr: InvalidLoginRequest){
                        _eventFlow.emit(UiEvent.ShowDialog(ilr.message ?: "Error"))
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
        object Login: UiEvent()
    }
}