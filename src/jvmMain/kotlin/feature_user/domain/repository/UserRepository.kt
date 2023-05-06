package feature_user.domain.repository

import core.model.BaseResponse
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.model.User
import feature_user.domain.model.UserResponse

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest, callback: (User?, response: BaseResponse) -> Unit)
    suspend fun registry(userRegistryRequest: UserResponse, callback: (response: BaseResponse) -> Unit)
    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest, callback: (response: BaseResponse) -> Unit)
}