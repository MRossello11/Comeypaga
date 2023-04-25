package feature_user.data.data_source

import core.Constants
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserDataSource {
    @POST("${Constants.WebService.BASE_URL}${Constants.WebService.USER}/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}