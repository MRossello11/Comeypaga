package feature_user.presentation.login

import core.model.BaseResponse
import feature_user.domain.model.User

data class LoginState(
    val username: String = "",
    val password: String = "",
    val user: User? = null,
    val loginResponse: BaseResponse = BaseResponse()
)