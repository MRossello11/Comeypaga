package feature_user.domain.use_cases

import core.Utils
import core.model.BaseResponse
import feature_user.domain.model.Order
import feature_user.domain.repository.UserOrderRepository

class GetHistoricOrdersUser(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        userId: String,
        callback: (response: BaseResponse, orders: ArrayList<Order>) -> Unit
    ) {
        userOrderRepository.getHistoricOrders(
            userId = userId,
            callback = { response, ordersWrapper ->
                val ordersToReturn = Utils.mapOrdersWsToOrders(ordersWrapper?.orders ?: listOf())
                callback(response, ordersToReturn)
            }
        )
    }
}