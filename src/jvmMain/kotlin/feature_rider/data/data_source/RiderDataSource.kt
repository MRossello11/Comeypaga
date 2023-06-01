package feature_rider.data.data_source

import core.Constants
import core.model.BaseResponse
import feature_rider.domain.model.UpdateOrderStateRiderRequest
import feature_user.domain.model.RiderOrders
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RiderDataSource {
    @GET("${Constants.WebService.BASE_URL}${Constants.WebService.ORDERS}${Constants.WebService.RIDER}/{riderId}")
    suspend fun getOrdersRider(
        @Path("riderId") riderId: String
    ): Response<RiderOrders>
    @POST("${Constants.WebService.BASE_URL}${Constants.WebService.ORDERS}${Constants.WebService.RIDER}")
    suspend fun updateOrderState(
        @Body updateOrderStateRiderRequest: UpdateOrderStateRiderRequest
    ): Response<BaseResponse>
}