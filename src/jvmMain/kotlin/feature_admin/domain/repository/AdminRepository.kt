package feature_admin.domain.repository

import core.model.BaseResponse
import core.model.Restaurant
import feature_admin.domain.model.PlateRequest

interface AdminRepository {
    suspend fun getRestaurant(restaurantId: String, callback: (response: BaseResponse, restaurant: Restaurant?) -> Unit)
    suspend fun getRestaurants(callback: (response: BaseResponse, restaurants: ArrayList<Restaurant>) -> Unit)
    suspend fun postRestaurant(restaurant: Restaurant, callback: (response: BaseResponse) -> Unit)
    suspend fun putRestaurant(restaurant: Restaurant, callback: (response: BaseResponse) -> Unit)
    suspend fun deleteRestaurant(restaurantId: String, callback: (response: BaseResponse) -> Unit)
    suspend fun putPlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit)
    suspend fun postPlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit)
    suspend fun deletePlate(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit)
}