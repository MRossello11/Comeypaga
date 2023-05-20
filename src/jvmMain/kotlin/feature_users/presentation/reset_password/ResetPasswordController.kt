package feature_users.presentation.reset_password

import feature_users.domain.model.InvalidResetPasswordRequest
import feature_users.domain.model.ResetPasswordRequest
import feature_users.domain.use_cases.UserUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ResetPasswordController(
    private val userUseCases: UserUseCases
) {

    private val _resetPasswordState = MutableStateFlow(ResetPasswordState())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
                    try {
                        userUseCases.resetPassword(
                            resetPasswordRequest = ResetPasswordRequest(
                                username = _resetPasswordState.value.username,
                                newPassword = _resetPasswordState.value.password,
                                passwordConfirmation = _resetPasswordState.value.passwordConfirmation
                            ),
                            callback = {
                                _resetPasswordState.update { state ->
                                    state.copy(
                                        resetPasswordResponse = it,
                                    )
                                }

                                launch{
                                    _eventFlow.emit(UiEvent.ShowDialog(it.message!!))
                                }
                            }
                        )

                    } catch (irpr: InvalidResetPasswordRequest){
                        _eventFlow.emit(UiEvent.ShowDialog(irpr.message ?: "Error"))
                    }
                }
            }
        }

    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
    }
}