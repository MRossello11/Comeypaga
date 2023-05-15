package feature_admin.data.data_source

import core.Constants.WebService
import core.model.BaseResponse
import core.model.Restaurant
import core.model.RestaurantWrapper
import core.model.RestaurantsWrapper
import feature_admin.domain.model.PlateRequest
import retrofit2.Response
import retrofit2.http.*

interface AdminDataSource {
    @GET("${WebService.BASE_URL}${WebService.RESTAURANTS}/{id}")
    suspend fun getRestaurant(
        @Path("id") _id: String
    ): Response<RestaurantWrapper>

    @GET("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun getRestaurants(): Response<RestaurantsWrapper>

    @POST("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun postRestaurant(
        @Body restaurant: Restaurant
    ): Response<RestaurantWrapper>

    @PUT("${WebService.BASE_URL}${WebService.RESTAURANTS}")
    suspend fun putRestaurant(
        @Body restaurant: Restaurant
    ): Response<RestaurantWrapper>

    @DELETE("${WebService.BASE_URL}${WebService.RESTAURANTS}/{id}")
    suspend fun deleteRestaurant(
        @Path("id") _id: String
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