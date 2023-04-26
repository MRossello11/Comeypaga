package feature_user.domain.repository

import feature_user.domain.model.LoginRequest
import feature_user.domain.model.User

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest, callback: (User?, errorCode: Int) -> Unit)
    fun registry()
}