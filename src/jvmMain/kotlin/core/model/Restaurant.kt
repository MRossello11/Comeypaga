package core.model

data class Restaurant(
    val _id: String? = null,
    val name: String,
    val foodType: String,
    val typology: String,
    val reviewStars: String,
    val phone: String,
    val email: String,
    val address: Address,
    val picture: String? = null,
    val menu: List<Plate>?
)

data class RestaurantWrapper(
    val restaurants: List<Restaurant>
)
class InvalidRestaurant(message: String): Exception(message)