package feature_admin.data

import com.google.gson.Gson
import core.handleBaseResponse
import core.model.BaseResponse
import core.model.ErrorResponse
import core.model.Restaurant
import feature_admin.data.data_source.AdminDataSource
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.repository.AdminRepository
import feature_users.domain.model.UserResponse

class AdminRepositoryImpl(
    private val adminDataSource: AdminDataSource
): AdminRepository {
    override suspend fun getRestaurant(restaurantId: String, callback: (response: BaseResponse, restaurant: Restaurant?) -> Unit) {
        val response = adminDataSource.getRestaurant(restaurantId)

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

    override suspend fun getRestaurants(callback: (response: BaseResponse, restaurants: ArrayList<Restaurant>) -> Unit) {
        val response = adminDataSource.getRestaurants()

        if (response.code() in 200..299){
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), it.restaurants as ArrayList<Restaurant>)
            } ?: run{
                callback(BaseResponse(response.code(), "An error occurred"), arrayListOf())
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"), arrayListOf())
        }
    }

    override suspend fun postRestaurant(restaurant: Restaurant, callback: (response: BaseResponse, newRestaurant: Restaurant?) -> Unit) {
        val response = adminDataSource.postRestaurant(restaurant)

        if (response.code() in 200..299) {
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), response.body()?.restaurant)
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

    override suspend fun putRestaurant(restaurant: Restaurant, callback: (response: BaseResponse, newRestaurant: Restaurant?) -> Unit) {
        val response = adminDataSource.putRestaurant(restaurant)

        if (response.code() in 200..299) {
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), response.body()?.restaurant)
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

    override suspend fun getRiders(callback: (response: BaseResponse, riders: List<UserResponse>) -> Unit) {
        val response = adminDataSource.getRiders()

        if (response.code() in 200..299){
            response.body()?.let {
                callback(BaseResponse(response.code(), response.message()), it.riders)
            } ?: run{
                callback(BaseResponse(response.code(), "An error occurred"), listOf())
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"), listOf())
        }
    }

    override suspend fun postRider(rider: UserResponse, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.postRider(rider)

        handleBaseResponse(response, callback)
    }

    override suspend fun deleteRider(riderId: String, callback: (response: BaseResponse) -> Unit) {
        val response = adminDataSource.deleteRider(riderId)

        handleBaseResponse(response, callback)
    }
}