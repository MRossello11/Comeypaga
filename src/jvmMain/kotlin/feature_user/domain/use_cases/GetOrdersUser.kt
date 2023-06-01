package feature_user.domain.use_cases

import core.Utils.mapOrdersWsToOrders
import core.model.BaseResponse
import feature_user.domain.model.Order
import feature_user.domain.repository.UserOrderRepository

class GetOrdersUser(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        userId: String,
        callback: (response: BaseResponse, orders: ArrayList<Order>) -> Unit
    ) {
        userOrderRepository.getOrdersUser(
            userId = userId,
            callback = { response, ordersWrapper ->
                val ordersToReturn = mapOrdersWsToOrders(ordersWrapper?.orders ?: listOf())
                callback(response, ordersToReturn)
            }
        )
    }
}