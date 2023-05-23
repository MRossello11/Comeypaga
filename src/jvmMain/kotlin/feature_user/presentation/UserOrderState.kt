package feature_user.presentation

import core.model.BaseResponse
import feature_user.domain.model.Order

data class UserOrderState(
    val order: Order,
    val response: BaseResponse = BaseResponse()
)
