package feature_users.presentation.login

sealed class LoginEvent{
    data class UsernameEntered(val value: String): LoginEvent()
    data class PasswordEntered(val value: String): LoginEvent()
    data class Login(val username: String, val password: String): LoginEvent()
}
