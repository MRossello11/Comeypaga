package feature_rider.domain.use_cases

import core.model.BaseResponse
import feature_rider.domain.model.UpdateOrderStateRiderRequest
import feature_rider.domain.repository.RiderRepository

class UpdateOrderState(
    private val riderRepository: RiderRepository
) {

    suspend operator fun invoke(
        updateOrderStateRiderRequest: UpdateOrderStateRiderRequest,
        callback: (response: BaseResponse) -> Unit
    ) {
        riderRepository.updateOrderState(updateOrderStateRiderRequest, callback)
    }
}