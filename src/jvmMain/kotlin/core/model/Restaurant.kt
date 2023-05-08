package core.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("foodType")
    val foodType: String,
    @SerializedName("typology")
    val typology: String,
    @SerializedName("reviewStars")
    val reviewStars: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: Address,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("menu")
    val menu: List<Plate>
)

data class RestaurantWrapper(
    @SerializedName("restaurants")
    val restaurants: List<Restaurant>
)
class InvalidRestaurant(message: String): Exception(message)