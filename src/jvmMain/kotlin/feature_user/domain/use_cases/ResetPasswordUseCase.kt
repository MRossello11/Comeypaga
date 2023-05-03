package feature_user.domain.use_cases

import core.model.BaseResponse
import feature_user.domain.model.InvalidResetPasswordRequest
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.repository.UserRepository

class ResetPasswordUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        resetPasswordRequest: ResetPasswordRequest,
        callback: (response: BaseResponse) -> Unit
    ){
        // validate fields
        if (resetPasswordRequest.username.isEmpty()){
            throw InvalidResetPasswordRequest("Username cannot be empty")
        }
        if (resetPasswordRequest.newPassword.isEmpty()){
            throw InvalidResetPasswordRequest("Password cannot be empty")
        }
        if (resetPasswordRequest.passwordConfirmation.isEmpty()){
            throw InvalidResetPasswordRequest("Password confirmation cannot be empty")
        }
        if (resetPasswordRequest.newPassword != resetPasswordRequest.passwordConfirmation){
            throw InvalidResetPasswordRequest("Passwords do not match")
        }

        // send request
        userRepository.resetPassword(
            resetPasswordRequest, callback
        )
    }
}