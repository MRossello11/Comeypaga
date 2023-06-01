package feature_rider.domain.use_cases

import core.Utils.mapOrdersWsToOrders
import core.model.BaseResponse
import feature_rider.domain.repository.RiderRepository
import feature_user.domain.model.Order

class GetOrders(
    private val riderRepository: RiderRepository
) {
    suspend operator fun invoke(
        riderId: String,
        callback: (response: BaseResponse, allOrders: List<Order>, ordersRider: List<Order>) -> Unit
    ) {

        riderRepository.getOrders(
            riderId = riderId,
            callback = { response, riderOrders ->
                val allOrders = mapOrdersWsToOrders(riderOrders?.allOrders ?: listOf())
                val ordersRider = mapOrdersWsToOrders(riderOrders?.riderOrders ?: listOf())

                callback(response, allOrders, ordersRider)
            }
        )
    }


}