package feature_admin.domain.use_cases

import core.model.BaseResponse
import core.model.InvalidRestaurant
import core.model.Restaurant
import feature_admin.domain.repository.AdminRepository
import java.io.File
import java.nio.file.Files
import java.util.*

class AddRestaurant(
    private val adminRepository: AdminRepository
){
    suspend operator fun invoke(
        restaurant: Restaurant, callback: (response: BaseResponse, newRestaurant: Restaurant?) -> Unit, newRestaurant: Boolean
    ){
        // verify fields
        if (restaurant.name.isEmpty()){
            throw InvalidRestaurant("Name cannot be empty")
        }
        if (restaurant.foodType.isEmpty()){
            throw InvalidRestaurant("Food type cannot be empty")
        }
        if (restaurant.typology.isEmpty()){
            throw InvalidRestaurant("Typology cannot be empty")
        }
        if (restaurant.phone.isEmpty()){
            throw InvalidRestaurant("Phone cannot be empty")
        }
        if (restaurant.email.isEmpty()){
            throw InvalidRestaurant("Email cannot be empty")
        }
        if (restaurant.address.street.isEmpty()){
            throw InvalidRestaurant("Street cannot be empty")
        }
        if (restaurant.address.town.isEmpty()){
            throw InvalidRestaurant("Town cannot be empty")
        }
        var picture = ""
        restaurant.picture?.let {
            if (it.isEmpty()){
                throw InvalidRestaurant("You need to add a picture")
            } else {
                // encode image
                val imageFile = File(restaurant.picture)
                val imageBytes = Files.readAllBytes(imageFile.toPath())
                picture = Base64.getEncoder().encodeToString(imageBytes)
            }
        } ?: kotlin.run {
            throw InvalidRestaurant("You need to add a picture")
        }
        // todo
        /*if (restaurant.menu.isEmpty()){
            throw InvalidRestaurant("Menu cannot be empty")
        }*/

        // verify email
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!emailPattern.matches(restaurant.email)) {
            throw InvalidRestaurant("Invalid email")
        }

        // verify phone
        val phonePattern = Regex("^((\\+34)|(0034))?([6|7|8|9][0-9]{8})\$")
        if (!phonePattern.matches(restaurant.phone)) {
            throw InvalidRestaurant("Invalid phone number")
        }

        // verify stars
        val stars = restaurant.reviewStars.toFloatOrNull() ?: throw InvalidRestaurant("Invalid review stars")

        if (stars !in 0f..5f) throw InvalidRestaurant("Invalid review stars")

        if (newRestaurant){
            adminRepository.putRestaurant(restaurant.copy(
                picture = picture
            ), callback)
        } else {
            adminRepository.postRestaurant(restaurant.copy(
                picture = picture
            ), callback)
        }
    }
}