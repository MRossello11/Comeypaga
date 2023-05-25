package feature_user.domain.use_cases

import core.Constants
import core.model.BaseResponse
import feature_user.domain.model.Order
import feature_user.domain.model.OrderLine
import feature_user.domain.repository.UserOrderRepository
import java.text.SimpleDateFormat

class GetOrdersUser(
    private val userOrderRepository: UserOrderRepository
) {
    suspend operator fun invoke(
        userId: String,
        callback: (response: BaseResponse, orders: ArrayList<Order>) -> Unit
    ) {
        // get string date
        val dbDate = SimpleDateFormat(Constants.DB_DATE)

        userOrderRepository.getOrdersUser(
            userId = userId,
            callback = { response, ordersWrapper ->
                val ordersToReturn = arrayListOf<Order>()
                // map OrdersWrapper to ArrayList<Order>
                ordersWrapper?.orders?.forEach { orderWS ->
                    val orderLines = arrayListOf<OrderLine>()
                    orderWS.orderLines.forEach { orderLineWS ->
                        orderLines.add(
                            OrderLine(
                                plateId = orderLineWS.plateId,
                                plateName = orderLineWS.plateName,
                                quantity = orderLineWS.quantity,
                                price = orderLineWS.price.toFloat()
                            )
                        )

                    }

                    ordersToReturn.add(
                        Order(
                            _id = orderWS._id,
                            shippingAddress = orderWS.shippingAddress,
                            state = orderWS.state,
                            arrivalTime = dbDate.parse(orderWS.arrivalTime),
                            restaurantId = orderWS.restaurantId,
                            restaurantName = orderWS.restaurantName,
                            userId = orderWS.userId,
                            orderLines = orderLines
                        )
                    )
                }
                callback(response, ordersToReturn)
            }
        )
    }
}