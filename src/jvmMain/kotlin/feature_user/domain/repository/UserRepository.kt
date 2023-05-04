package feature_user.domain.repository

import core.model.BaseResponse
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.model.User

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest, callback: (User?, errorCode: Int, errorMessage: String) -> Unit)
    fun registry()
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest, callback: (response: BaseResponse) -> Unit)
}