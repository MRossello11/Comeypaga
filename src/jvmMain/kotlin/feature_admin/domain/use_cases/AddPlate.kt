package feature_admin.domain.use_cases

import core.model.BaseResponse
import feature_admin.domain.model.InvalidPlateRequest
import feature_admin.domain.model.PlateRequest
import feature_admin.domain.repository.AdminRepository

class AddPlate(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        plateRequest: PlateRequest,
        callback: (response: BaseResponse) -> Unit,
        newPlate: Boolean
    ){
        // verify field
        if (plateRequest.plateName?.isEmpty() == true){
            throw InvalidPlateRequest("Plate name cannot be empty")
        }

        val price = plateRequest.price?.toFloatOrNull()
        price?.let {
            if (it < 0){
                throw InvalidPlateRequest("Invalid price")
            }
        } ?: run {
            throw InvalidPlateRequest("Invalid price")
        }

        if (plateRequest.type?.isEmpty() == true){
            throw InvalidPlateRequest("Plate type cannot be empty")
        }

        if (newPlate){
            adminRepository.putPlate(plateRequest, callback)
        } else {
            adminRepository.postPlate(plateRequest, callback)
        }
    }
}