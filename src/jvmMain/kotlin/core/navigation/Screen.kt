package core.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import core.model.Plate
import core.model.Restaurant
import feature_user.domain.model.Order
import feature_users.domain.model.Role
import feature_users.domain.model.User
import feature_users.domain.model.UserResponse

sealed class Screen: Parcelable {
    @Parcelize
    object Login: Screen()
    @Parcelize
    object UserResetPassword: Screen()
    @Parcelize
    data class Registry(val userRole: Role, val user: UserResponse = UserResponse()): Screen()
    @Parcelize
    data class UserMain(val user: User): Screen()
    @Parcelize
    data class RiderMain(val rider: User): Screen()
    @Parcelize
    data class AdminMain(val admin: User): Screen()
    @Parcelize
    data class AddModifyRestaurant(val restaurant: Restaurant?): Screen()
    @Parcelize
    data class MenuScreen(val restaurant: Restaurant): Screen()
    @Parcelize
    data class AddModifyPlateScreen(val restaurant: Restaurant?, val plate: Plate?): Screen()
    @Parcelize
    data class RestaurantDetailsScreen(val restaurant: Restaurant): Screen()
    @Parcelize
    data class OrderDetailsScreen(val order: Order): Screen()
}
