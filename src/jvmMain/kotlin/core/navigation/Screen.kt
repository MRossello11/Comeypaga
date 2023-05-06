package core.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import feature_user.domain.model.User

sealed class Screen: Parcelable {
    @Parcelize
    object Login: Screen()
    @Parcelize
    object UserResetPassword: Screen()
    @Parcelize
    object Registry: Screen()
    @Parcelize
    data class UserMain(val user: User): Screen()
    @Parcelize
    data class RiderMain(val rider: User): Screen()
    @Parcelize
    data class AdminMain(val admin: User): Screen()
}
