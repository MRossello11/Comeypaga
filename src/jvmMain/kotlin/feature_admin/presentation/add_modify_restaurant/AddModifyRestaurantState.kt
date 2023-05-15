package feature_admin.presentation.add_modify_restaurant

import core.model.BaseResponse
import core.model.Restaurant

data class AddModifyRestaurantState(
    val restaurant: Restaurant,
    val shouldNavigateToMenu: Boolean = false,
    val addModifyRestaurantResponse: BaseResponse = BaseResponse()
)
