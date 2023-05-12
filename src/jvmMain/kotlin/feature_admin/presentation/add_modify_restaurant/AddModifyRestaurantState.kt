package feature_admin.presentation.add_modify_restaurant

import core.model.BaseResponse
import core.model.Plate

data class AddModifyRestaurantState(
    val name: String = "",
    val foodType: String = "",
    val typology: String = "",
    val reviewStars: String = "",
    val phone: String = "",
    val email: String = "",
    val street: String = "",
    val town: String = "",
    val menu: List<Plate> = listOf(),
    val addModifyRestaurantResponse: BaseResponse = BaseResponse()
)
