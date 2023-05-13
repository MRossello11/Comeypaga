package feature_admin.presentation.add_modify_restaurant

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles.spacerModifier
import core.ComeypagaStyles.standardDialogDimension
import core.components.AppHeader
import core.components.LabeledTextField
import core.components.PrimaryButton
import core.components.dialogs.OneOptionDialog
import core.model.Address
import core.model.Restaurant
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddModifyRestaurantScreen(
    controller: AddRestaurantController,
    restaurant: Restaurant? = null,
    onBack: () -> Unit,
    onClickEditMenu: (Restaurant) -> Unit
) {
    val viewState = controller.state.collectAsState()
    var state by remember { mutableStateOf(AddModifyRestaurantState()) }

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        controller.eventFlow.collectLatest { event ->
            when(event){
                AddRestaurantController.UiEvent.RestaurantCreated -> {
                    errorDialogMessage = viewState.value.addModifyRestaurantResponse.message ?: "Restaurant created!"
                    showDialog = true
                }
                is AddRestaurantController.UiEvent.ShowDialog -> {
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
        this.window.size = standardDialogDimension
        OneOptionDialog(
            text = errorDialogMessage,
            onClickButton = {
                showDialog = false

                if(viewState.value.addModifyRestaurantResponse.errorCode in 200..299){
                    onBack()
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = "Edit restaurant",
            onClickBack = onBack
        )

        // todo: image
        Box(
            modifier = Modifier.fillMaxWidth().height(120.dp)
        ) {
            Text(text = "(IMAGE)")
        }

        // form
        Row {
            // left column
            Column(
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabeledTextField(
                    value = state.name,
                    label = "Name",
                    onValueChange = {
                        state = state.copy(
                            name = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.NAME))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.typology,
                    label = "Typology",
                    onValueChange = {
                        state = state.copy(
                            typology = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.TYPOLOGY))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.email,
                    label = "Email",
                    onValueChange = {
                        state = state.copy(
                            email = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.EMAIL))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.town,
                    label = "Town",
                    onValueChange = {
                        state = state.copy(
                            town = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.TOWN))
                    }
                )
            }

            // right column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabeledTextField(
                    value = state.foodType,
                    label = "Food type",
                    onValueChange = {
                        state = state.copy(
                            foodType = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.FOOD_TYPE))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.reviewStars,
                    label = "Stars",
                    onValueChange = {
                        state = state.copy(
                            reviewStars = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.REVIEW_STARS))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.phone,
                    label = "Phone",
                    onValueChange = {
                        state = state.copy(
                            phone = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.PHONE))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.street,
                    label = "Street",
                    onValueChange = {
                        state = state.copy(
                            street = it
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(value = it, field = RestaurantField.STREET))
                    }
                )
            }
        }

        // buttons
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 10.dp),
                content = "Edit menu",
                onClick = {
                    onClickEditMenu(
                        restaurant ?: Restaurant(
                            _id = null,
                            name = state.name,
                            foodType = state.foodType,
                            typology = state.typology,
                            reviewStars = state.reviewStars,
                            phone = state.phone,
                            email = state.email,
                            address = Address(
                                street = state.street,
                                town = state.town
                            ),
                            picture = "", // todo
                            menu = listOf(),
                        )
                    )
                }
            )

            Spacer(modifier = spacerModifier)

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
                        controller.onEvent(AddModifyRestaurantEvent.CreateRestaurant)
                    }
                )
            }

            Spacer(modifier = spacerModifier)
        }
    }
}