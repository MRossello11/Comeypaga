package feature_user.presentation.login

import feature_user.domain.model.InvalidLoginRequest
import feature_user.domain.model.LoginRequest
import feature_user.domain.use_cases.UserUseCases
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

            LoginEvent.Login -> {
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        userUseCases.loginUseCase(
                            loginRequest = LoginRequest(_loginState.value.username, _loginState.value.password),
                            callback = { user, errorCode, errorMessage ->
                                _loginState.update { currentState ->
                                    currentState.copy(
                                        wsReturnCode = errorCode,
                                        user = user,
                                        errorMessage = errorMessage
                                    )
                                }
                            }
                        )
                        if (_loginState.value.wsReturnCode in 200..299){
                            _eventFlow.emit(UiEvent.Login)
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(_loginState.value.errorMessage.ifEmpty { "Error" }))
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