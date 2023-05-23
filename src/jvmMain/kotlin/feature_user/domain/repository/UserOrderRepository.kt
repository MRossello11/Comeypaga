package feature_user.domain.repository

import core.model.BaseResponse
import feature_user.domain.model.OrderWS

interface UserOrderRepository {
    suspend fun updateOrder(order: OrderWS, callback: (response: BaseResponse, order: OrderWS?) -> Unit)
    suspend fun cancelOrder(orderId: String, callback: (response: BaseResponse) -> Unit)
}