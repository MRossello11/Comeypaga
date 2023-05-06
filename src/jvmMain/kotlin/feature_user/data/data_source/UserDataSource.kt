package feature_user.data.data_source

import core.Constants
import core.model.BaseResponse
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserDataSource {
    @POST("${Constants.WebService.BASE_URL}${Constants.WebService.USER}/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<UserResponse>

    @POST("${Constants.WebService.BASE_URL}${Constants.WebService.USER}/resetPassword")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<BaseResponse>

    @PUT("${Constants.WebService.BASE_URL}${Constants.WebService.USER}/registry")
    suspend fun registry(
        @Body userRegistryRequest: UserResponse
    ): Response<BaseResponse>
}