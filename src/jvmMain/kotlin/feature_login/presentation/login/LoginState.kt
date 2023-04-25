package feature_login.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val wsReturnCode: Int? = null
)