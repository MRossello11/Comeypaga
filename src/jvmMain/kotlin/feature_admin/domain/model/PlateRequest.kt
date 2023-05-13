package feature_admin.domain.model

import com.google.gson.annotations.SerializedName

data class PlateRequest(
    @SerializedName("restaurantId")
    val restaurantId: String,
    @SerializedName("plateId")
    val plateId: String,
    @SerializedName("plateName")
    val plateName: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("price")
    val price: Float? = null,
    @SerializedName("type")
    val type: String? = null
)

class InvalidPlateRequest(message: String): Exception(message)