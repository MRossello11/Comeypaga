package feature_users.domain.model

import core.model.Address

data class UserResponse(
    val _id: String,
    val username: String,
    val firstname: String,
    val lastname: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val address: Address,
    val password: String,
    val role: String = Role.User.toString()
)

data class RiderList(
    val riders: List<UserResponse>
)

class InvalidUser(message: String): Exception(message)