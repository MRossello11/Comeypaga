package feature_rider.domain.model

data class UpdateOrderStateRiderRequest(
    // order id
    val _id: String,
    // new state
    val newState: Int,
    // riderId
    val riderId: String
)
