package feature_login.presentation.view

sealed class LoginEvent{
    data class UsernameEntered(val value: String): LoginEvent()
    data class PasswordEntered(val value: String): LoginEvent()
    object Login: LoginEvent()
}
