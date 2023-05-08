package feature_admin.presentation.restaurants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import core.components.AppHeader
import core.components.OneOptionDialog
import core.components.restaurants.RestaurantCard
import kotlinx.coroutines.flow.collectLatest
import java.awt.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRestaurantScreen(
    controller: AdminRestaurantsController,
    onBack: () -> Unit
){
    val viewState: AdminRestaurantsState by controller.state.collectAsState()

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true){
        controller.eventFlow.collectLatest { event ->
            when(event){
                is AdminRestaurantsController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }
            }
        }
    }

    Dialog(
        title = "Aviso",
        visible = showDialog,
        onCloseRequest = {
            showDialog = false
        },
    ) {
        this.window.size = Dimension(325, 150)
        OneOptionDialog(
            text = errorDialogMessage,
            onClickButton = {
                showDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppHeader(
            title = "Restaurants temp",
            onClickBack = onBack
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewState.restaurants){restaurant ->
                RestaurantCard(
                    modifier = Modifier.fillMaxWidth(),
                    restaurant = restaurant,
                    canDelete = true,
                    onDeleteClick = {}
                )
            }
        }
    }
}