package feature_users.presentation.reset_password

sealed class ResetPasswordEvent {
    data class FieldEntered(val value: String, val field: ResetPasswordField): ResetPasswordEvent()

    object ResetPassword: ResetPasswordEvent()

}
