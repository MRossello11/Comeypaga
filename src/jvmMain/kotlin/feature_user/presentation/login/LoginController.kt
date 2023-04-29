package feature_user.presentation.login

import feature_user.domain.model.LoginRequest
import feature_user.domain.use_cases.UserUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginController(
    private val userUseCases: UserUseCases
) {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

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
                    userUseCases.loginUseCase(
                        loginRequest = LoginRequest(_loginState.value.username, _loginState.value.password),
                        callback = { user, errorCode ->
                            _loginState.update { currentState ->
                                currentState.copy(
                                    wsReturnCode = errorCode,
                                    user = user
                                )
                            }
                        }
                    )
                }
            }
        }
    }



}