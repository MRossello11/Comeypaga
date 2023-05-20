package feature_admin.domain.use_cases

import core.model.BaseResponse
import core.model.Restaurant
import feature_admin.domain.repository.AdminRepository

class GetRestaurants(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        callback: (response: BaseResponse, restaurants: ArrayList<Restaurant>) -> Unit
    ){
        adminRepository.getRestaurants(callback)
    }
}