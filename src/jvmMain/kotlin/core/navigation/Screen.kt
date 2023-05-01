package core.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Screen: Parcelable {
    @Parcelize
    object Login: Screen()
    @Parcelize
    object UserResetPassword: Screen()
}
