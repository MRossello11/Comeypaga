package feature_admin.presentation.add_modify_plate

import core.model.BaseResponse
import core.model.Plate
import core.model.Restaurant

data class AddModifyPlateState(
    val restaurant: Restaurant? = null,
    val plate: Plate? = null,
    val response: BaseResponse = BaseResponse()
)