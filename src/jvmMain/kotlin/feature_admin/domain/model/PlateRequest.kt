package feature_admin.domain.model

import com.google.gson.annotations.SerializedName

data class PlateRequest(
    @SerializedName("plateId")
    val restaurantId: String,
    @SerializedName("plateId")
    val plateId: String,
    @SerializedName("plateName")
    val plateName: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: Float,
    @SerializedName("type")
    val type: String
)
