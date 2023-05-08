package feature_admin.domain.use_cases

import core.model.BaseResponse
import feature_admin.domain.model.InvalidPlateRequest
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.repository.AdminRepository

class AddPlate(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(plateRequest: PlateRequest, callback: (response: BaseResponse) -> Unit){
        // verify field
        if (plateRequest.plateName.isEmpty()){
            throw InvalidPlateRequest("Plate name cannot be empty")
        }
        if (plateRequest.price < 0){
            throw InvalidPlateRequest("Invalid price")
        }
        if (plateRequest.type.isEmpty()){
            throw InvalidPlateRequest("Plate type cannot be empty")
        }

        adminRepository.putPlate(plateRequest, callback)
    }
}