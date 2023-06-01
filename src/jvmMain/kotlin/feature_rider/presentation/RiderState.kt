package feature_rider.presentation

import core.model.BaseResponse
import feature_user.domain.model.Order

data class RiderState(
    val allOrders: List<Order>,
    val riderOrders: List<Order>,
    val response: BaseResponse = BaseResponse()
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RiderState

        if (allOrders != other.allOrders) return false
        if (riderOrders != other.riderOrders) return false
        if (response != other.response) return false

        return true
    }

    override fun hashCode(): Int {
        var result = allOrders.hashCode()
        result = 31 * result + riderOrders.hashCode()
        result = 31 * result + response.hashCode()
        return result
    }
}
