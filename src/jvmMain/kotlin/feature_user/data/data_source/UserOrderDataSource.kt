package feature_user.data.data_source

import core.Constants
import core.model.BaseResponse
import feature_user.domain.model.OrderWS
import feature_user.domain.model.OrderWrapper
import retrofit2.Response
import retrofit2.http.*

interface UserOrderDataSource {
    @GET("${Constants.WebService.BASE_URL}${Constants.WebService.ORDERS}${Constants.WebService.USER}")
    suspend fun getOrdersUser(
        @Body order: OrderWS // just needed to get the userId
    ): Response<OrderWrapper>
    @POST("${Constants.WebService.BASE_URL}${Constants.WebService.ORDERS}${Constants.WebService.USER}")
    suspend fun updateOrder(
        @Body order: OrderWS
    ): Response<OrderWrapper>

    @DELETE("${Constants.WebService.BASE_URL}${Constants.WebService.ORDERS}${Constants.WebService.USER}/{id}")
    suspend fun cancelOrder(
        @Path("id") _id: String
    ): Response<BaseResponse>
}