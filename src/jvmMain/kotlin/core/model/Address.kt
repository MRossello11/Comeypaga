package core.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("street")
    val street: String,

    @SerializedName("town")
    val town: String,
)
