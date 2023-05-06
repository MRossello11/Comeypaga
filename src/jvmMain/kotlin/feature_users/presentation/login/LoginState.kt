package feature_users.presentation.login

import core.model.BaseResponse
import feature_users.domain.model.User

data class LoginState(
    val username: String = "",
    val password: String = "",
    val user: User? = null,
    val loginResponse: BaseResponse = BaseResponse()
)