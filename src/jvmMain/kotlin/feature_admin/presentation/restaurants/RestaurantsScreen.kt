package feature_admin.presentation.restaurants

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.ComeypagaStyles.spacerModifier
import core.ComeypagaStyles.standardDialogDimension
import core.ComeypagaStyles.standardTwoOptionsDialogDimension
import core.components.dialogs.OneOptionDialog
import core.components.dialogs.TwoOptionDialog
import core.components.restaurants.RestaurantCard
import core.model.Restaurant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantsScreen(
    controller: AdminRestaurantsController,
    onAddRestaurant: () -> Unit,
    onClickRestaurant: (Restaurant) -> Unit,
    editMode: Boolean = false
){
    val restaurants = remember { mutableStateOf(listOf<Restaurant>()) }

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var showTwoOptionsDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true){
        controller.eventFlow.collectLatest { event ->
            when(event){
                is AdminRestaurantsController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }
                AdminRestaurantsController.UiEvent.RestaurantDeleted -> {
                    CoroutineScope(Dispatchers.IO).launch{
                        // get updated list
                        val result = controller.getRestaurants()

                        // set list
                        result?.let {
                            restaurants.value = it
                        }
                    }
                }
                is AdminRestaurantsController.UiEvent.ShowDeleteRestaurantDialogConfirmation -> {
                    showTwoOptionsDialog = true
                    errorDialogMessage = event.message
                }
            }
        }
    }

    // launched effect to get restaurants
    LaunchedEffect(Unit){
        val result = controller.getRestaurants()

        // evaluate result
        result?.let {
            if (result.isNotEmpty()){
                // found restaurants
                restaurants.value = it
            } else {
                // didn't find restaurants (response is ok, but empty)
                showDialog = true
                errorDialogMessage = "No restaurants found"
            }
        } ?: run {
            // there was an error
            showDialog = true
            errorDialogMessage = "Error getting restaurants"
        }
    }

    Dialog(
        title = "Aviso",
        visible = showDialog,
        onCloseRequest = {
            showDialog = false
        },
    ) {
        this.window.size = standardDialogDimension
        OneOptionDialog(
            text = errorDialogMessage,
            onClickButton = {
                showDialog = false
            }
        )
    }
    Dialog(
        title = "Aviso",
        visible = showTwoOptionsDialog,
        onCloseRequest = {
            showTwoOptionsDialog = false
        },
    ) {
        this.window.size = standardTwoOptionsDialogDimension
        TwoOptionDialog(
            text = errorDialogMessage,
            onClickPositive = {
                // delete restaurant
                controller.onEvent(AdminRestaurantsEvent.DeletionConfirmed)
                showTwoOptionsDialog = false
            },
            onClickNegative = {
                showTwoOptionsDialog = false
            }
        )
    }

    Scaffold (
        floatingActionButton = {
            if (editMode) {
                FloatingActionButton(
                    modifier = Modifier,
                    onClick = onAddRestaurant,
                    shape = CircleShape,
                    containerColor = ComeypagaStyles.primaryColorGreen,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add restaurant"
                    )
                }
            }
        }
    ) {
        Column {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(restaurants.value) { restaurant ->
                    Spacer(modifier = spacerModifier)
                    RestaurantCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClickRestaurant(restaurant)
                            },
                        restaurant = restaurant,
                        canDelete = editMode,
                        onDeleteClick = {
                            controller.onEvent(AdminRestaurantsEvent.DeleteRestaurant(restaurant))
                        }
                    )
                    Spacer(modifier = spacerModifier)
                }
            }
        }
    }
}