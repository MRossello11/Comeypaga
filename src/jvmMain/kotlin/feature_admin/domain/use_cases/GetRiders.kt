package feature_admin.domain.use_cases

import core.model.BaseResponse
import feature_admin.domain.repository.AdminRepository
import feature_users.domain.model.UserResponse

class GetRiders(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        callback: (response: BaseResponse, riders: List<UserResponse>) -> Unit
    ) {
        adminRepository.getRiders(callback)
    }
}