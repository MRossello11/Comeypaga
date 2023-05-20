package feature_users.domain.model

import core.model.Address
import java.util.*

data class User(
    val _id: String,
    val username: String,

    val firstname: String,

    val lastname: String,

    val birthDate: Date,

    val phone: String,

    val email: String,

    val address: Address,

    val password: String,

    val role: Role = Role.User
)
