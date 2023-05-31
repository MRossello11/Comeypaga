package feature_user.presentation

import core.model.BaseResponse
import feature_user.domain.model.Order

data class UserOrderState(
    val order: Order, // order the user is modifying
    val response: BaseResponse = BaseResponse(),
    val ordersInCurse: List<Order> = listOf() // orders in progress
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserOrderState

        if (order != other.order) return false
        if (response != other.response) return false
        if (ordersInCurse != other.ordersInCurse) return false

        return true
    }

    override fun hashCode(): Int {
        var result = order.hashCode()
        result = 31 * result + response.hashCode()
        result = 31 * result + ordersInCurse.hashCode()
        return result
    }
}
