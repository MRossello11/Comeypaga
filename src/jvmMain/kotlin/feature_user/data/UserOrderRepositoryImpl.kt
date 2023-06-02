package feature_user.data

import com.google.gson.Gson
import core.handleBaseResponse
import core.model.BaseResponse
import core.model.ErrorResponse
import core.model.Restaurant
import feature_user.data.data_source.UserOrderDataSource
import feature_user.domain.model.OrderWS
import feature_user.domain.model.OrdersWrapper
import feature_user.domain.repository.UserOrderRepository
import retrofit2.Response

class UserOrderRepositoryImpl(
    private val userOrderDataSource: UserOrderDataSource
): UserOrderRepository {
    override suspend fun getOrdersUser(userId: String, callback: (response: BaseResponse, orders: OrdersWrapper?) -> Unit) {
        val response = userOrderDataSource.getOrdersUser(userId)

        handleOrdersWrapperResponse(response, callback)

    }

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

    override suspend fun getRestaurantData(
        restaurantId: String,
        callback: (response: BaseResponse, restaurant: Restaurant?) -> Unit
    ) {
        val response = userOrderDataSource.getRestaurant(restaurantId)

        if (response.code() in 200..299){
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), it.restaurant)
            } ?: run{
                callback(BaseResponse(response.code(), "An error occurred"), null)
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"), null)
        }
    }

    override suspend fun getHistoricOrders(
        userId: String,
        callback: (response: BaseResponse, orders: OrdersWrapper?) -> Unit
    ) {
        val response = userOrderDataSource.getHistoricOrdersUser(userId)

        handleOrdersWrapperResponse(response, callback)
    }

    private fun handleOrdersWrapperResponse(
        response: Response<OrdersWrapper>,
        callback: (response: BaseResponse, orders: OrdersWrapper?) -> Unit
    ) {
        if (response.code() in 200..299) {
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), response.body())
            } ?: run {
                callback(BaseResponse(response.code(), "An error occurred"), null)
            }
        } else if (response.code() == 401 || response.code() == 403) {
            callback(BaseResponse(response.code(), "You are not authorized"), null)
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"), null)
        }
    }
}