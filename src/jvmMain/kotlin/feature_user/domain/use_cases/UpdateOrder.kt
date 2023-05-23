package feature_user.domain.use_cases

import core.Constants
import core.Constants.OrderStates.CREATED
import core.Constants.OrderStates.IN_PROGRESS
import core.model.BaseResponse
import feature_user.domain.model.Order
import feature_user.domain.model.OrderWS
import feature_user.domain.repository.UserOrderRepository
import java.text.SimpleDateFormat

class UpdateOrder(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        order: Order,
        callback: (response: BaseResponse, order: OrderWS?) -> Unit
    ) {
        // order state is verified on server

        // get string date
        val dbDate = SimpleDateFormat(Constants.DB_DATE)

        // map Order to OrderWS
        val orderWS = OrderWS(
            _id = order._id,
            shippingAddress = order.shippingAddress,
            arrivalTime = dbDate.format(order.arrivalTime),
            state = order._id?.let { IN_PROGRESS } ?: run { CREATED },
            restaurantId = order.restaurantId,
            restaurantName = order.restaurantName,
            userId = order.userId,
            orderLines = order.orderLines
        )

        // send request
        userOrderRepository.updateOrder(orderWS, callback)
    }
}