package feature_users.presentation.registry

import core.model.BaseResponse
import feature_users.domain.model.UserResponse

data class RegistryState(
    val user: UserResponse = UserResponse(),
    val registryResponse: BaseResponse = BaseResponse()
)
