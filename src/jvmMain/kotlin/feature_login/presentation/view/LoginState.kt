package feature_login.presentation.view

data class LoginState(
    val username: String = "",
    val password: String = "",
    val wsReturnCode: Int? = null
)