package feature_admin.domain.repository

import core.model.BaseResponse
import core.model.Restaurant
import feature_admin.domain.model.PlateRequest
import feature_users.domain.model.UserResponse

interface AdminRepository {
    suspend fun getRestaurant(restaurantId: String, callback: (response: BaseResponse, restaurant: Restaurant?) -> Unit)
    suspend fun getRestaurants(callback: (response: BaseResponse, restaurants: ArrayList<Restaurant>) -> Unit)
    suspend fun postRestaurant(restaurant: Restaurant, callback: (response: BaseResponse, newRestaurant: Restaurant?) -> Unit)
    suspend fun putRestaurant(restaurant: Restaurant, callback: (response: BaseResponse, newRestaurant: Restaurant?) -> Unit)
    suspend fun deleteRestaurant(restaurantId: String, callback: (response: BaseResponse) -> Unit)
    suspend fun putPlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit)
    suspend fun postPlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit)
    suspend fun deletePlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit)
    suspend fun getRiders(callback: (response: BaseResponse, riders: List<UserResponse>) -> Unit)
    suspend fun postRider(rider: UserResponse, callback: (response: BaseResponse) -> Unit)
    suspend fun deleteRider(riderId: String, callback: (response: BaseResponse) -> Unit)
}