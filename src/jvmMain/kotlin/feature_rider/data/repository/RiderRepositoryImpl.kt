package feature_rider.data.repository

import com.google.gson.Gson
import core.handleBaseResponse
import core.model.BaseResponse
import core.model.ErrorResponse
import feature_rider.data.data_source.RiderDataSource
import feature_rider.domain.model.UpdateOrderStateRiderRequest
import feature_rider.domain.repository.RiderRepository
import feature_user.domain.model.RiderOrders

class RiderRepositoryImpl(
    private val riderDataSource: RiderDataSource
): RiderRepository {
    override suspend fun getOrders(riderId: String, callback: (response: BaseResponse, orders: RiderOrders?) -> Unit) {
        val response = riderDataSource.getOrdersRider(riderId)

        if (response.code() in 200..299) {
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), response.body())
            } ?: run {
                callback(BaseResponse(response.code(), "An error occurred"), null)
            }
        } else if(response.code() == 401 || response.code() == 403){
            callback(BaseResponse(response.code(), "You are not authorized"), null)
        }else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"), null)
        }
    }

    override suspend fun updateOrderState(
        updateOrderStateRiderRequest: UpdateOrderStateRiderRequest,
        callback: (response: BaseResponse) -> Unit
    ) {
        val response = riderDataSource.updateOrderState(updateOrderStateRiderRequest)

        handleBaseResponse(response, callback)
    }
}