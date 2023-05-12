package feature_admin.presentation.add_modify_restaurant

sealed class AddModifyRestaurantEvent {
    data class FieldEntered(val value: String, val field: RestaurantField): AddModifyRestaurantEvent()
    object CreateRestaurant: AddModifyRestaurantEvent()
    object ModifyRestaurant: AddModifyRestaurantEvent()
}