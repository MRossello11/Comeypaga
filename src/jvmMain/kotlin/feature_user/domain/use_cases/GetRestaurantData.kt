package feature_user.domain.use_cases

import core.model.BaseResponse
import core.model.Restaurant
import feature_user.domain.repository.UserOrderRepository

class GetRestaurantData(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        restaurantId: String,
        callback: (response: BaseResponse, restaurant: Restaurant?) -> Unit
    ) {
        userOrderRepository.getRestaurantData(
            restaurantId, callback
        )
    }
}