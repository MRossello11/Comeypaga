package feature_user.presentation.ticket

import core.model.Address
import core.model.BaseResponse
import core.model.Restaurant
import feature_user.domain.model.Order

data class TicketState(
    val order: Order,
    val restaurant: Restaurant = Restaurant("","","","","","","", Address("",""),"", listOf()),
    val response: BaseResponse = BaseResponse()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TicketState

        if (order != other.order) return false
        if (restaurant != other.restaurant) return false
        if (response != other.response) return false

        return true
    }

    override fun hashCode(): Int {
        var result = order.hashCode()
        result = 31 * result + restaurant.hashCode()
        result = 31 * result + response.hashCode()
        return result
    }
}
