package feature_login.data.data_source

import feature_login.domain.model.LoginRequest
import feature_login.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginDataSource {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}