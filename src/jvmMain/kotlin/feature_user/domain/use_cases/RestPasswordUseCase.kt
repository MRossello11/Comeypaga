package feature_user.domain.use_cases

import core.model.BaseResponse
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.repository.UserRepository

class RestPasswordUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        resetPasswordRequest: ResetPasswordRequest,
        callback: (response: BaseResponse) -> Unit
    ){
        userRepository.resetPassword(
            resetPasswordRequest, callback
        )
    }
}