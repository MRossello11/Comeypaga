package feature_users.presentation.reset_password
import core.model.BaseResponse

data class ResetPasswordState(
    val username: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val resetPasswordResponse: BaseResponse = BaseResponse()
)
