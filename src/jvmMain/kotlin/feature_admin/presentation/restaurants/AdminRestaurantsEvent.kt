package feature_admin.presentation.restaurants

import core.model.Restaurant

sealed class AdminRestaurantsEvent{
//    data class QueryRestaurants(val query: String): AdminRestaurantsEvent()
    data class DeleteRestaurant(val restaurant: Restaurant): AdminRestaurantsEvent()
    object DeletionConfirmed: AdminRestaurantsEvent()
}
