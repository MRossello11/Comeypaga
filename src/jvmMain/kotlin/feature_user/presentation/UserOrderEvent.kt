package feature_user.presentation

import core.model.Restaurant
import feature_user.domain.model.Order
import feature_user.domain.model.OrderLine

sealed class UserOrderEvent{
    data class UpdateOrder(val restaurant: Restaurant? = null, val newOrderLine: OrderLine): UserOrderEvent()
    data class CancelOrder(val order: Order): UserOrderEvent()
    data class SendOrder(val order: Order): UserOrderEvent()
}
