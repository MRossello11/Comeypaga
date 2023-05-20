package feature_admin.domain.use_cases

import core.model.BaseResponse
import core.model.InvalidRestaurant
import core.model.Restaurant
import feature_admin.domain.repository.AdminRepository

class GetRestaurant(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        restaurant: Restaurant,
        callback: (response: BaseResponse, restaurant: Restaurant?) -> Unit
    ) {
        restaurant._id?.let {
            adminRepository.getRestaurant(it, callback)
        } ?: run {
            throw InvalidRestaurant("Invalid id")
        }
    }
}