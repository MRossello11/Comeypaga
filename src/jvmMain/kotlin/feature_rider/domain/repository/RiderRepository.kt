package feature_rider.domain.repository

import core.model.BaseResponse
import feature_rider.domain.model.UpdateOrderStateRiderRequest
import feature_user.domain.model.RiderOrders

interface RiderRepository {
    suspend fun getOrders(riderId: String, callback: (response: BaseResponse, orders: RiderOrders?) -> Unit)
    suspend fun updateOrderState(updateOrderStateRiderRequest: UpdateOrderStateRiderRequest, callback: (response: BaseResponse) -> Unit)
}