package feature_admin.domain.use_cases

import core.Utils
import core.model.BaseResponse
import feature_users.domain.model.UserResponse
import feature_users.domain.repository.UserRepository
@Deprecated("Better use UserUseCases.registryUseCase")
class AddRider(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        rider: UserResponse, callback: (response: BaseResponse) -> Unit
    ) {
        // verify request
        Utils.verifyUser(rider)
        val dateToSend: String = Utils.getDateFromUserResponse(rider)

        userRepository.registry(
            rider.copy(
                birthDate = dateToSend
            ),
            callback
        )
    }
}