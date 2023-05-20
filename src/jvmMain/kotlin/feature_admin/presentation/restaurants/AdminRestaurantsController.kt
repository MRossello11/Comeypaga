package feature_admin.presentation.restaurants

import androidx.compose.ui.graphics.toComposeImageBitmap
import core.model.Restaurant
import feature_admin.domain.use_cases.AdminUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.imageio.ImageIO

class AdminRestaurantsController(
    private val adminUseCases: AdminUseCases
) {
    private val _state = MutableStateFlow(AdminRestaurantsState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AdminRestaurantsEvent){
        when(event){
            is AdminRestaurantsEvent.DeletionConfirmed -> {
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        // delete restaurant
                        adminUseCases.deleteRestaurant(
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        response = it,
                                    )
                                }
                            },
                            restaurantId = _state.value.actualRestaurant!!._id!!
                        )

                        // handle response
                        if (_state.value.response.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.ShowDialog("Restaurant '${_state.value.actualRestaurant!!.name}' deleted"))
                            _eventFlow.emit(UiEvent.RestaurantDeleted)

                            _state.update { state ->
                                state.copy(
                                    actualRestaurant = null
                                )
                            }
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.response.message ?: "An error occurred deleting restaurant"))
                        }
                    } catch (e: Exception){
                        _eventFlow.emit(UiEvent.ShowDialog("An error occurred deleting restaurant"))
                    }
                }
            }
            is AdminRestaurantsEvent.DeleteRestaurant -> {
                _state.update { state ->
                    state.copy(
                        actualRestaurant = event.restaurant
                    )
                }
                // show dialog confirmation
                CoroutineScope(Dispatchers.IO).launch {
                    _eventFlow.emit(UiEvent.ShowDeleteRestaurantDialogConfirmation("Confirm deletion of restaurant '${event.restaurant.name}'?"))
                }
            }
        }
    }

    suspend fun getRestaurants(): List<Restaurant>?{
        try {
            adminUseCases.getRestaurants(
                callback = { response, restaurants ->
                    try {
                        restaurants.forEachIndexed { index, restaurant ->
                            restaurant.picture?.let {
                                if (it.isNotEmpty()) {
                                    // decode picture
                                    val decodedBytes = Base64.getDecoder().decode(it)
                                    val bufferedImage = ImageIO.read(decodedBytes.inputStream())

                                    // set image bitmap
                                    restaurants[index] = restaurant.copy(
                                        imageBitmap = bufferedImage.toComposeImageBitmap(),
                                    )
                                }
                            }
                        }
                    } catch (_: Exception){}

                    _state.update { state ->
                        state.copy(
                            response = response,
                            restaurants = restaurants
                        )
                    }
                }
            )
            if (_state.value.response.errorCode in 200..299) {
                return _state.value.restaurants
            } else {
                _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "Error getting restaurants"))
            }
        } catch (e: Exception){
            _eventFlow.emit(UiEvent.ShowDialog("Error getting restaurants"))
        }
        return null
    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()
        data class ShowDeleteRestaurantDialogConfirmation(val message: String): UiEvent()
        object RestaurantDeleted: UiEvent()
    }
}