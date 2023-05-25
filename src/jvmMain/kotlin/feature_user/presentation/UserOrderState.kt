package feature_user.presentation

import core.model.BaseResponse
import feature_user.domain.model.Order

data class UserOrderState(
    val order: Order, // order the user is modifying
    val response: BaseResponse = BaseResponse(),
    val ordersInCurse: ArrayList<Order> = arrayListOf() // orders in progress
)
