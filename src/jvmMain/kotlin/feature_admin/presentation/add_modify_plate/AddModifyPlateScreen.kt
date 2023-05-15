package feature_admin.presentation.add_modify_plate

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.ComeypagaStyles.spacerModifier
import core.components.AppHeader
import core.components.DropMenu
import core.components.LabeledTextField
import core.components.PrimaryButton
import core.components.dialogs.OneOptionDialog
import core.model.Address
import core.model.Plate
import core.model.Restaurant
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddModifyPlateScreen(
    controller: AddModifyPlateController,
    restaurant: Restaurant = Restaurant("","","","","","","", Address("",""),"", listOf()),
    plate: Plate = Plate("","","","",""),
    newPlate: Boolean,
    onBack: () -> Unit
) {
    val viewState = controller.state.collectAsState()
    var state by remember { mutableStateOf(
        AddModifyPlateState(
            restaurant = restaurant,
            plate = plate
        )
    ) }

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        controller.eventFlow.collectLatest { event ->
            when(event){
                AddModifyPlateController.UiEvent.PlateCreated -> {
                    showDialog = true
                    errorDialogMessage = viewState.value.response.message ?: "Plate created"
                }
                is AddModifyPlateController.UiEvent.ShowDialog -> {
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
        this.window.size = ComeypagaStyles.standardDialogDimension
        OneOptionDialog(
            text = errorDialogMessage,
            onClickButton = {
                showDialog = false

                if(viewState.value.response.errorCode in 200..299){
                    onBack()
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = "Edit plate",
            onClickBack = onBack
        )

        // form
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabeledTextField(
                    value = state.plate?.plateName!!,
                    label = "Name",
                    onValueChange = {
                        state = state.copy(
                            plate = state.plate?.copy(
                                plateName = it
                            )
                        )
                        controller.onEvent(AddModifyPlateEvent.FieldEntered(state.plate!!))
                    }
                )
                Spacer(modifier = spacerModifier)
                LabeledTextField(
                    value = state.plate?.price!!,
                    label = "Price",
                    onValueChange = {
                        state = state.copy(
                            plate = state.plate?.copy(
                                price = it
                            )
                        )
                        controller.onEvent(AddModifyPlateEvent.FieldEntered(state.plate!!))
                    }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabeledTextField(
                    value = state.plate?.description!!,
                    label = "Description",
                    onValueChange = {
                        state = state.copy(
                            plate = state.plate?.copy(
                                description = it
                            )
                        )
                        controller.onEvent(AddModifyPlateEvent.FieldEntered(state.plate!!))
                    }
                )
                Spacer(modifier = spacerModifier)
                DropMenu(
                    items = listOf("starter", "main", "drink", "dessert"),
                    onItemClick = {
                        state = state.copy(
                            plate = state.plate?.copy(
                                type = it
                            )
                        )
                        controller.onEvent(AddModifyPlateEvent.FieldEntered(state.plate!!))
                    }
                )
            }
        }

        // buttons
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Row {
                PrimaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(horizontal = 10.dp),
                    content = "Cancel",
                    onClick = onBack, // todo: dialog?
                    backgroundColor = Color.Red
                )

                PrimaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(horizontal = 10.dp),
                    content = "Save",
                    onClick = {
                        if (newPlate) {
                            controller.onEvent(AddModifyPlateEvent.CreatePlate)
                        } else {
                            controller.onEvent(AddModifyPlateEvent.ModifyPlate)
                        }
                    }
                )
            }

            Spacer(modifier = spacerModifier)

        }

    }


}