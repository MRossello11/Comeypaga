package feature_user.presentation.reset_password
import core.model.BaseResponse

data class ResetPasswordState(
    val username: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val resetPasswordResponse: BaseResponse = BaseResponse(),
    val responseEventConsumed: Boolean = true
)
