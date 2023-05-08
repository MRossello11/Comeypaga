package feature_admin

import com.google.gson.Gson
import core.handleBaseResponse
import core.model.BaseResponse
import core.model.ErrorResponse
import core.model.Restaurant
import feature_admin.data.AdminDataSource
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.repository.AdminRepository

class AdminRepositoryImpl(
    private val adminDataSource: AdminDataSource
): AdminRepository {
    override suspend fun getRestaurants(callback: (response: BaseResponse, restaurants: ArrayList<Restaurant>) -> Unit) {
        val response = adminDataSource.getRestaurants()

        if (response.code() in 200..299){
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), it)
            } ?: run{
                callback(BaseResponse(response.code(), "An error occurred"), arrayListOf())
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"), arrayListOf())
        }
    }

    override suspend fun postRestaurant(restaurant: Restaurant, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.postRestaurant(restaurant)

        handleBaseResponse(response, callback)
    }

    override suspend fun putRestaurant(restaurant: Restaurant, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.putRestaurant(restaurant)

        handleBaseResponse(response, callback)
    }

    override suspend fun deleteRestaurant(restaurantId: String, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.deleteRestaurant(restaurantId)

        handleBaseResponse(response, callback)
    }

    override suspend fun putPlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.putPlate(plateRequest)

        handleBaseResponse(response, callback)
    }

    override suspend fun postPlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.postPlate(plateRequest)

        handleBaseResponse(response, callback)
    }

    override suspend fun deletePlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.deletePlate(plateRequest)

        handleBaseResponse(response, callback)

    }
}