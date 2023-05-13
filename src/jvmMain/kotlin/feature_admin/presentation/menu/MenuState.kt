package feature_admin.presentation.menu

import core.model.BaseResponse
import core.model.Plate
import core.model.Restaurant

data class MenuState(
    val restaurant: Restaurant? = null,
    val actualPlate: Plate? = null,
    val response: BaseResponse = BaseResponse()
)
