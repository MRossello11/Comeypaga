package feature_admin.presentation.add_modify_restaurant

import core.model.Address
import core.model.InvalidRestaurant
import core.model.Restaurant
import feature_admin.domain.use_cases.AdminUseCases
import feature_admin.presentation.add_modify_restaurant.RestaurantField.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddRestaurantController(
    private val adminUseCases: AdminUseCases
){

    private val _state = MutableStateFlow(AddModifyRestaurantState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: AddModifyRestaurantEvent){
        when(event){
            is AddModifyRestaurantEvent.FieldEntered -> {
                when(event.field){
                    NAME -> {
                        _state.update { state ->
                            state.copy(
                                name = event.value
                            )
                        }
                    }
                    FOOD_TYPE -> {
                        _state.update { state ->
                            state.copy(
                                foodType = event.value
                            )
                        }
                    }
                    TYPOLOGY -> {
                        _state.update { state ->
                            state.copy(
                                typology = event.value
                            )
                        }
                    }
                    REVIEW_STARS -> {
                        _state.update { state ->
                            state.copy(
                                reviewStars = event.value
                            )
                        }
                    }
                    PHONE -> {
                        _state.update { state ->
                            state.copy(
                                phone = event.value
                            )
                        }
                    }
                    EMAIL -> {
                        _state.update { state ->
                            state.copy(
                                email = event.value
                            )
                        }
                    }
                    STREET -> {
                        _state.update { state ->
                            state.copy(
                                street = event.value
                            )
                        }
                    }
                    TOWN -> {
                        _state.update { state ->
                            state.copy(
                                town = event.value
                            )
                        }
                    }
                }
            }

            AddModifyRestaurantEvent.CreateRestaurant -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // create restaurant
                        adminUseCases.addRestaurant(
                            restaurant = Restaurant(
                                name = _state.value.name,
                                foodType = _state.value.foodType,
                                typology = _state.value.typology,
                                reviewStars = _state.value.reviewStars,
                                phone = _state.value.phone,
                                email = _state.value.email,
                                address = Address(
                                    street = _state.value.street,
                                    town = _state.value.town
                                ),
                                picture = "", // todo
                                menu = _state.value.menu
                            ),
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        addModifyRestaurantResponse = it
                                    )
                                }
                            }
                        )

                        // handle response
                        if (_state.value.addModifyRestaurantResponse.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.RestaurantCreated)
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.addModifyRestaurantResponse.message ?: "An error occurred"))
                        }
                    } catch (ir: InvalidRestaurant){
                        ir.printStackTrace() // todo
                        _eventFlow.emit(UiEvent.ShowDialog(ir.message ?: "An error occurred"))
                    }
                }

            }

            AddModifyRestaurantEvent.ModifyRestaurant -> {}
        }
    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()

        object RestaurantCreated: UiEvent()
    }

}