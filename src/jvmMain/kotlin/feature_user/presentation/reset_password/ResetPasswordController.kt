package feature_user.presentation.reset_password

import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.use_cases.UserUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordController(
    private val userUseCases: UserUseCases
) {

    private val _resetPasswordState = MutableStateFlow(ResetPasswordState())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    fun onEvent(event: ResetPasswordEvent){
        when(event){
            is ResetPasswordEvent.FieldEntered -> {
                when(event.field){
                    ResetPasswordField.USERNAME -> {
                        _resetPasswordState.update { state ->
                            state.copy(
                                username = event.value
                            )
                        }
                    }

                    ResetPasswordField.PASSWORD -> {
                        _resetPasswordState.update { state ->
                            state.copy(
                                password = event.value
                            )
                        }
                    }

                    ResetPasswordField.PASSWORD_CONFIRMATION -> {
                        _resetPasswordState.update { state ->
                            state.copy(
                                passwordConfirmation = event.value
                            )
                        }
                    }
                }
            }
            is ResetPasswordEvent.ResetPassword -> {
                CoroutineScope(Dispatchers.IO).launch {
                    // todo validate
                    userUseCases.resetPassword(
                        resetPasswordRequest = ResetPasswordRequest(
                            username = _resetPasswordState.value.username,
                            newPassword = _resetPasswordState.value.password,
                        ),
                        callback = {
                            _resetPasswordState.update { state ->
                                state.copy(
                                    resetPasswordResponse = it,
                                    responseEventConsumed = false
                                )
                            }

                        }
                    )
                }
            }
        }

    }

    fun consumeResponseEvent(){
        _resetPasswordState.update { state ->
            state.copy(
                responseEventConsumed = true
            )
        }
    }
}