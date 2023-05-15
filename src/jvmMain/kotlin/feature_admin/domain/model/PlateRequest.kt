package feature_admin.domain.model


data class PlateRequest(
    val restaurantId: String,
    val plateId: String? = null,
    val plateName: String? = null,
    val description: String? = null,
    val price: String? = null,
    val type: String? = null
)

class InvalidPlateRequest(message: String): Exception(message)