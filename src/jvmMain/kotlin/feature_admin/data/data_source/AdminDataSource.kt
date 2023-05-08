package feature_admin.data.data_source

import core.Constants.WebService
import core.model.BaseResponse
import core.model.Restaurant
import feature_admin.domain.model.PlateRequest
import retrofit2.Response
import retrofit2.http.*

interface AdminDataSource {
    @GET("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun getRestaurants(): Response<ArrayList<Restaurant>>

    @POST("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun postRestaurant(
        @Body restaurant: Restaurant
    ): Response<BaseResponse>

    @PUT("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun putRestaurant(
        @Body restaurant: Restaurant
    ): Response<BaseResponse>

    @DELETE("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun deleteRestaurant(
        @Body id: String
    ): Response<BaseResponse>

    @PUT("${WebService.BASE_URL}${WebService.RESTAURANTS}${WebService.MENU}")
    suspend fun putPlate(
        @Body plateRequest: PlateRequest
    ): Response<BaseResponse>

    @POST("${WebService.BASE_URL}${WebService.RESTAURANTS}${WebService.MENU}")
    suspend fun postPlate(
        @Body plateRequest: PlateRequest
    ): Response<BaseResponse>

    @DELETE("${WebService.BASE_URL}${WebService.RESTAURANTS}${WebService.MENU}")
    suspend fun deletePlate(
        @Body plateRequest: PlateRequest
    ): Response<BaseResponse>
}