package feature_admin.presentation.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import core.components.AppHeader
import core.components.dialogs.OneOptionDialog
import core.components.dialogs.TwoOptionDialog
import core.components.restaurants.PlateListItem
import core.model.Plate
import core.model.Restaurant
import kotlinx.coroutines.flow.collectLatest
import java.awt.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    restaurant: Restaurant,
    controller: MenuController,
    onBack: () -> Unit,
    onClickPlate: (Restaurant, Plate) -> Unit,
    onClickAddPlate: (Restaurant) -> Unit
){
    val viewState: MenuState by controller.state.collectAsState()
    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var showTwoOptionsDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }


    LaunchedEffect(key1 = true){
        controller.eventFlow.collectLatest { event ->
            when(event){
                is MenuController.UiEvent.ShowDeletePlateDialogConfirmation -> {
                    showTwoOptionsDialog = true
                    errorDialogMessage = event.message
                }
                is MenuController.UiEvent.ShowDialog -> {
                    showDialog = true
                    errorDialogMessage = event.message
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

    Dialog(
        title = "Aviso",
        visible = showTwoOptionsDialog,
        onCloseRequest = {
            showTwoOptionsDialog = false
        },
    ) {
        this.window.size = Dimension(325, 150)
        TwoOptionDialog(
            text = errorDialogMessage,
            onClickPositive = {
                controller.onEvent(MenuEvent.DeletionConfirmed)
                showTwoOptionsDialog = false
            },
            onClickNegative = {
                showTwoOptionsDialog = false
            }
        )
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                onClick = { onClickAddPlate(restaurant) },
                shape = CircleShape,
                containerColor = ComeypagaStyles.primaryColorGreen,
                contentColor = Color.White
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add plate"
                )
            }
        }
    ) {
        Column {
            AppHeader(
                title = "Edit menu",
                onClickBack = onBack
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 5.dp, top = 10.dp),
            ) {
                items(viewState.restaurant?.menu ?: listOf()) { plate ->
                    Spacer(modifier = spacerModifier)

                    // plate list item
                    PlateListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                controller.onEvent(MenuEvent.SetPlate(plate))
                                onClickPlate(restaurant, plate)
                            },
                        plate = plate,
                        onDeletePlate = {
                            controller.onEvent(MenuEvent.DeletePlate(it))
                        },
                        editMode = true
                    )
                }
            }
        }
    }
}