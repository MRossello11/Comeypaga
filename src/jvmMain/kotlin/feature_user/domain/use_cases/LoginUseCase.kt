package feature_user.domain.use_cases

import feature_user.domain.model.LoginRequest
import feature_user.domain.model.User
import feature_user.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest, callback: (User?, errorCode: Int) -> Unit){
        userRepository.login(loginRequest, callback)
    }
}