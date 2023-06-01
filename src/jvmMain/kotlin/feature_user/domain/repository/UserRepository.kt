package feature_user.domain.repository

import core.model.BaseResponse
import core.model.Restaurant

interface UserRepository {
    suspend fun getRestaurants(callback: (response: BaseResponse, restaurants: ArrayList<Restaurant>) -> Unit)
}