package feature_user.domain.model

import com.google.gson.annotations.SerializedName
import core.model.Address
import java.util.Date

data class User(
    @SerializedName("username")
    val username: String,

    @SerializedName("firstname")
    val firstname: String,

    @SerializedName("lastname")
    val lastname: String,

    @SerializedName("birthDate")
    val birthDate: Date,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("address")
    val address: Address,

    @SerializedName("password")
    val password: String
)