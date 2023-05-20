package feature_users.domain.use_cases

import core.Utils
import core.Utils.getDateFromUserResponse
import core.model.BaseResponse
import feature_users.domain.model.UserResponse
import feature_users.domain.repository.UserRepository

class RegistryUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userRegistryRequest: UserResponse,
        callback: (response: BaseResponse) -> Unit
    ) {
        // verify request
        Utils.verifyUser(userRegistryRequest)
        val dateToSend: String = getDateFromUserResponse(userRegistryRequest)

        userRepository.registry(
            userRegistryRequest.copy(
                birthDate = dateToSend
            ),
            callback
        )
    }
}