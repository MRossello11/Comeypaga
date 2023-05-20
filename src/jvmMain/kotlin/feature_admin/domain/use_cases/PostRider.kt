package feature_admin.domain.use_cases

import core.Utils
import core.model.BaseResponse
import feature_admin.domain.repository.AdminRepository
import feature_users.domain.model.UserResponse

class PostRider(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        rider: UserResponse, callback: (response: BaseResponse) -> Unit
    ) {
        // verify request
        Utils.verifyUser(rider)
        val dateToSend: String = Utils.getDateFromUserResponse(rider)

        adminRepository.postRider(
            rider.copy(
                birthDate = dateToSend
            ),
            callback
        )
    }
}