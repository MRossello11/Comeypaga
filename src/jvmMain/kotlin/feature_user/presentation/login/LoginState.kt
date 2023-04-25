package feature_user.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val wsReturnCode: Int? = null
)