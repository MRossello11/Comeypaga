package feature_admin.presentation.add_modify_restaurant

import core.model.Restaurant

sealed class AddModifyRestaurantEvent {
    data class FieldEntered(val restaurant: Restaurant): AddModifyRestaurantEvent()
    object CreateRestaurant: AddModifyRestaurantEvent()
    object ModifyRestaurant: AddModifyRestaurantEvent()
}