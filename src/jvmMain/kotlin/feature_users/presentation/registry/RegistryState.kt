package feature_users.presentation.registry

import core.model.BaseResponse

data class RegistryState(
    val username: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val email: String = "",
    val street: String = "",
    val town: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val registryResponse: BaseResponse = BaseResponse()
)
