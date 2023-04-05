package feature_login.domain.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("errorCode")
    val errorCode: Int
)
