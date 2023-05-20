package feature_admin.domain.use_cases

import core.model.BaseResponse
import feature_admin.domain.repository.AdminRepository

class DeleteRider(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        riderId: String,
        callback: (response: BaseResponse) -> Unit
    ){
        adminRepository.deleteRider(riderId, callback)
    }
}