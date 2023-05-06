package core.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    val errorCode: Int? = null,

    @SerializedName("message")
    val message: String? = null
)
