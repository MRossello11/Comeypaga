package core.model

import com.google.gson.annotations.SerializedName

data class Plate(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: Float,
    @SerializedName("type")
    val type: String
)
