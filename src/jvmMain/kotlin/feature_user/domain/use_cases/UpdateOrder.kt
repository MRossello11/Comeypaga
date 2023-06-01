package feature_user.domain.use_cases

import core.Constants
import core.model.BaseResponse
import feature_user.domain.model.Order
import feature_user.domain.model.OrderLine
import feature_user.domain.model.OrderLineWS
import feature_user.domain.model.OrderWS
import feature_user.domain.repository.UserOrderRepository
import java.text.SimpleDateFormat

class UpdateOrder(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        order: Order,
        callback: (response: BaseResponse, order: Order?) -> Unit
    ) {
        // order state is verified on server

        // get string date
        val dbDate = SimpleDateFormat(Constants.DB_DATE)

        // map OrderLines to OrderLinesWS
        val orderLinesWS = arrayListOf<OrderLineWS>()

        order.orderLines.forEach {
            orderLinesWS.add(
                OrderLineWS(
                    plateId = it.plateId,
                    plateName = it.plateName,
                    quantity = it.quantity,
                    price = it.price.toString()
                )
            )
        }

        // map Order to OrderWS
        val orderWS = OrderWS(
            _id = order._id,
            shippingAddress = order.shippingAddress,
            arrivalTime = dbDate.format(order.arrivalTime),
            state = order.state,
            restaurantId = order.restaurantId,
            restaurantName = order.restaurantName,
            userId = order.userId,
            orderLines = orderLinesWS
        )

        // send request
        userOrderRepository.updateOrder(orderWS) { response, returnedOrderWS ->
            // map OrderLinesWS to OrderLine
            val orderLines = arrayListOf<OrderLine>()

            returnedOrderWS?.orderLines?.forEach {
                orderLines.add(
                    OrderLine(
                        plateId = it.plateId,
                        plateName = it.plateName,
                        quantity = it.quantity,
                        price = it.price.toFloat()
                    )
                )
            }

            // Map OrderWS to Order
            val orderToReturn = returnedOrderWS?.let {
                Order(
                    _id = it._id,
                    shippingAddress = it.shippingAddress,
                    state = it.state,
                    arrivalTime = dbDate.parse(it.arrivalTime),
                    restaurantId = it.restaurantId,
                    restaurantName = it.restaurantName,
                    userId = it.userId,
                    orderLines = orderLines
                )
            }

            // Invoke the original callback with the mapped Order object
            callback(response, orderToReturn)
        }
    }
}