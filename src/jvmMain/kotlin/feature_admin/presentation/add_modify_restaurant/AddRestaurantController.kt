package feature_admin.presentation.add_modify_restaurant

import core.model.Address
import core.model.InvalidRestaurant
import core.model.Restaurant
import feature_admin.domain.use_cases.AdminUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddRestaurantController(
    private val adminUseCases: AdminUseCases
){

    private val _state = MutableStateFlow(AddModifyRestaurantState(
        Restaurant("","","","","","","",Address("",""),"", listOf()),
    ))
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: AddModifyRestaurantEvent){
        when(event){
            is AddModifyRestaurantEvent.FieldEntered -> {
                _state.update { state ->
                    state.copy(
                        restaurant = event.restaurant
                    )
                }
            }

            AddModifyRestaurantEvent.CreateRestaurant -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // create restaurant
                        adminUseCases.addRestaurant(
                            restaurant = _state.value.restaurant,
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        addModifyRestaurantResponse = it
                                    )
                                }
                            },
                            newRestaurant = true
                        )

                        // handle response
                        if (_state.value.addModifyRestaurantResponse.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.RestaurantCreated)
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.addModifyRestaurantResponse.message ?: "An error occurred"))
                        }
                    } catch (ir: InvalidRestaurant){
                        _eventFlow.emit(UiEvent.ShowDialog(ir.message ?: "An error occurred"))
                    }
                }

            }

            AddModifyRestaurantEvent.ModifyRestaurant -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // create restaurant
                        adminUseCases.addRestaurant(
                            restaurant = _state.value.restaurant,
                            callback = {
                                _state.update { state ->
                                    state.copy(
                                        addModifyRestaurantResponse = it
                                    )
                                }
                            },
                            newRestaurant = false
                        )

                        // handle response
                        if (_state.value.addModifyRestaurantResponse.errorCode in 200..299){
                            _eventFlow.emit(UiEvent.RestaurantCreated)
                        } else {
                            _eventFlow.emit(UiEvent.ShowDialog(message = _state.value.addModifyRestaurantResponse.message ?: "An error occurred"))
                        }
                    } catch (ir: InvalidRestaurant){
                        _eventFlow.emit(UiEvent.ShowDialog(ir.message ?: "An error occurred"))
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()

        object RestaurantCreated: UiEvent()
    }

}