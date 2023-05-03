package feature_user.domain.model

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("username")
    val username: String = "",
    @SerializedName("newPassword")
    val newPassword: String = ""
)