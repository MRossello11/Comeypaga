package feature_user.domain.model

import core.model.Address
import java.util.*

data class Order(
    val _id: String? = null,
    val shippingAddress: Address = Address("", ""),
    val state: Int = 0,
    val arrivalTime: Date = Date(),
    val restaurantId: String = "",
    val restaurantName: String = "",
    val userId: String,
    val riderId: String = "",
    val orderLines: ArrayList<OrderLine> = arrayListOf()
)

data class OrderLine(
    val plateId: String = "",
    val plateName: String = "",
    var quantity: Int = 0,
    val price: Float = 0f
) {
    override fun equals(other: Any?): Boolean {
        if (other is OrderLine) {
            return this.plateId == other.plateId
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

data class OrderLineWS(
    val plateId: String = "",
    val plateName: String = "",
    var quantity: Int = 0,
    val price: String = ""
)

// Order object to send to the web server
data class OrderWS(
    val _id: String? = null,
    val shippingAddress: Address = Address("", ""),
    val state: Int = 0,
    val arrivalTime: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val userId: String,
    val riderId: String = "",
    val orderLines: List<OrderLineWS> = listOf()
)

data class OrderWrapper(
    val order: OrderWS
)
data class OrdersWrapper(
    val orders: List<OrderWS>
)

class InvalidOrderModification(message: String): Exception(message)