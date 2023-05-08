package feature_admin.presentation.restaurants

import feature_admin.domain.use_cases.AdminUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AdminRestaurantsController(
    private val adminUseCases: AdminUseCases
) {
    private val _state = MutableStateFlow(AdminRestaurantsState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        println("Init")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Calling get rest")
                adminUseCases.getRestaurants(
                    callback = { response, restaurants ->
                        _state.update { state ->
                            state.copy(
                                response = response,
                                restaurants = restaurants
                            )
                        }
                        println("Gottem $restaurants")
                    }
                )
                if (_state.value.response.errorCode !in 200..299) {
                    _eventFlow.emit(UiEvent.ShowDialog(_state.value.response.message ?: "Error getting restaurants"))
                }
            } catch (e: Exception){
                e.printStackTrace()
                _eventFlow.emit(UiEvent.ShowDialog("Error getting restaurants"))
            }
        }
    }
    sealed class UiEvent{
        data class ShowDialog(val message: String): UiEvent()

    }
}