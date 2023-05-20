package core.model

import androidx.compose.ui.graphics.ImageBitmap

data class Restaurant(
    val _id: String? = null,
    val name: String,
    val foodType: String,
    val typology: String,
    val reviewStars: String,
    val phone: String,
    val email: String,
    val address: Address,
    val picture: String? = null, // base64 string image
    val menu: List<Plate>?,
    @Transient
    val imageBitmap: ImageBitmap? = null // ImageBitmap of the image
)

data class RestaurantsWrapper(
    val restaurants: List<Restaurant>
)

data class RestaurantWrapper(
    val restaurant: Restaurant
)
class InvalidRestaurant(message: String): Exception(message)