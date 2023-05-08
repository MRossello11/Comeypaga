package feature_admin.domain.use_cases

import core.model.BaseResponse
import feature_admin.domain.repository.AdminRepository

class DeleteRestaurant(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(restaurantId: String, callback: (response: BaseResponse) -> Unit){
        adminRepository.deleteRestaurant(restaurantId, callback)
    }
}