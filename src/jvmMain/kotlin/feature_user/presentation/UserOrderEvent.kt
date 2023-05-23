package feature_user.presentation

import feature_user.domain.model.Order

sealed class UserOrderEvent{
    data class UpdateOrder(val order: Order): UserOrderEvent()
    data class CancelOrder(val order: Order): UserOrderEvent()
    data class SendOrder(val order: Order): UserOrderEvent()
}
