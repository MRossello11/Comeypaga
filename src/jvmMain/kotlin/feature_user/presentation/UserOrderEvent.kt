package feature_user.presentation

import core.model.Restaurant
import feature_user.domain.model.OrderLine

sealed class UserOrderEvent{
    data class UpdateOrder(val restaurant: Restaurant? = null, val newOrderLine: OrderLine): UserOrderEvent()
    object CancelOrder: UserOrderEvent()
    object SendOrder: UserOrderEvent()
}
