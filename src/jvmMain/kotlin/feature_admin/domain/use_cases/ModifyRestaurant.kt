package feature_admin.domain.use_cases

import core.model.BaseResponse
import core.model.Restaurant
import feature_admin.domain.repository.AdminRepository

class ModifyRestaurant(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(restaurant: Restaurant, callback: (response: BaseResponse) -> Unit){
        adminRepository.postRestaurant(restaurant, callback)
    }
}