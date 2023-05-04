package feature_user.domain.use_cases

import feature_user.domain.model.InvalidLoginRequest
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.User
import feature_user.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest, callback: (User?, errorCode: Int, errorMessage: String) -> Unit){
        if (loginRequest.username.isEmpty()){
            throw InvalidLoginRequest("Username cannot be empty")
        }
        
        if (loginRequest.password.isEmpty()){
            throw InvalidLoginRequest("Password cannot be empty")
        }

        userRepository.login(loginRequest, callback)
    }
}