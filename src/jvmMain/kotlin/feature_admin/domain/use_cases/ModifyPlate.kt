package feature_admin.domain.use_cases

import core.model.BaseResponse
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.repository.AdminRepository

class ModifyPlate(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit){
        adminRepository.postPlate(plateRequest, callback)
    }
}