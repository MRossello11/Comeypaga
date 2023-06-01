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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        if (_id != other._id) return false
        if (shippingAddress != other.shippingAddress) return false
        if (state != other.state) return false
        if (arrivalTime != other.arrivalTime) return false
        if (restaurantId != other.restaurantId) return false
        if (restaurantName != other.restaurantName) return false
        if (userId != other.userId) return false
        if (riderId != other.riderId) return false
        if (orderLines != other.orderLines) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id?.hashCode() ?: 0
        result = 31 * result + shippingAddress.hashCode()
        result = 31 * result + state
        result = 31 * result + arrivalTime.hashCode()
        result = 31 * result + restaurantId.hashCode()
        result = 31 * result + restaurantName.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + riderId.hashCode()
        result = 31 * result + orderLines.hashCode()
        return result
    }
}

data class OrderLine(
    val plateId: String = "",
    val plateName: String = "",
    var quantity: Int = 0,
    val price: Float = 0f
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderLine

        if (plateId != other.plateId) return false
        if (plateName != other.plateName) return false
        if (quantity != other.quantity) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = plateId.hashCode()
        result = 31 * result + plateName.hashCode()
        result = 31 * result + quantity
        result = 31 * result + price.hashCode()
        return result
    }

    override fun toString(): String {
        return "OrderLine{plateId:$plateId,plateName:$plateName,qty:$quantity"
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

data class RiderOrders(
    val riderOrders: List<OrderWS>,
    val allOrders: List<OrderWS>
)

class InvalidOrderModification(message: String): Exception(message)