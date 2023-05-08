package feature_admin.presentation.restaurants

import core.model.BaseResponse
import core.model.Restaurant

data class AdminRestaurantsState(
    val restaurants: List<Restaurant> = listOf(),
    val query: String = "",
    val response: BaseResponse = BaseResponse()
)
