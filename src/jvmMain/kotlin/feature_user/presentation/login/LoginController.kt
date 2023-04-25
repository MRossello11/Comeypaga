package feature_user.presentation.login

import feature_user.data.data_source.UserDataSource
import feature_user.domain.model.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class LoginController(retrofit: Retrofit) {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val userDataSource: UserDataSource = retrofit.create(UserDataSource::class.java)


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
                    val response = userDataSource.login(
                        LoginRequest(
                            username =  _loginState.value.username,
                            password =  _loginState.value.password,
                        )
                    )
                    _loginState.update { currentState ->
                        currentState.copy(
                            wsReturnCode = response.errorCode
                        )
                    }
                }
            }
        }
    }



}