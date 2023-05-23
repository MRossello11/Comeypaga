package feature_user.data

import com.google.gson.Gson
import core.handleBaseResponse
import core.model.BaseResponse
import core.model.ErrorResponse
import feature_user.data.data_source.UserOrderDataSource
import feature_user.domain.model.OrderWS
import feature_user.domain.repository.UserOrderRepository

class UserOrderRepositoryImpl(
    private val userOrderDataSource: UserOrderDataSource
): UserOrderRepository {

    override suspend fun updateOrder(order: OrderWS, callback: (response: BaseResponse, order: OrderWS?) -> Unit) {
        val response = userOrderDataSource.updateOrder(order)

        if (response.code() in 200..299) {
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), response.body()?.order)
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

    override suspend fun cancelOrder(orderId: String, callback: (response: BaseResponse) -> Unit) {
        val response = userOrderDataSource.cancelOrder(orderId)

        handleBaseResponse(response, callback)
    }
}