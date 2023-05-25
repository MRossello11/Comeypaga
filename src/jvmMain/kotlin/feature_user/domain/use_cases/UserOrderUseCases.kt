package feature_user.domain.use_cases

data class UserOrderUseCases(
    val getOrdersUser: GetOrdersUser,
    val updateOrder: UpdateOrder,
    val cancelOrder: CancelOrder
)
