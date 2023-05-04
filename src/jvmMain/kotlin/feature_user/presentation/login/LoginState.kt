package feature_user.presentation.login

import feature_user.domain.model.User

data class LoginState(
    val username: String = "",
    val password: String = "",
    val wsReturnCode: Int? = null,
    val user: User? = null,
    val errorMessage: String = ""
)