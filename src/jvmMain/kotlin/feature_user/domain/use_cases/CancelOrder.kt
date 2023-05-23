package feature_user.domain.use_cases

import core.model.BaseResponse
import feature_user.domain.model.InvalidOrderModification
import feature_user.domain.model.Order
import feature_user.domain.repository.UserOrderRepository

class CancelOrder(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        order: Order,
        callback: (response: BaseResponse) -> Unit
    ) {
        order._id?.let {
            userOrderRepository.cancelOrder(it, callback)
        } ?: run {
            throw InvalidOrderModification("Cannot cancel order")
        }
    }
}