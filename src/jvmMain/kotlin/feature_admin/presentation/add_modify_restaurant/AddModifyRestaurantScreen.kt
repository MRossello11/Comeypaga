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
    restaurant: Restaurant = Restaurant("","","","","","","",Address("",""),"", listOf()),
    onBack: () -> Unit,
    onClickEditMenu: (Restaurant) -> Unit,
    newRestaurant: Boolean = false
) {
    val viewState = controller.state.collectAsState()
    var state by remember { mutableStateOf(
        AddModifyRestaurantState(
            restaurant = restaurant
        )
    ) }

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
                    value = state.restaurant.name,
                    label = "Name",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                name = it
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = restaurant))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.restaurant.typology,
                    label = "Typology",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                typology = it
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = state.restaurant))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.restaurant.email,
                    label = "Email",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                email = it
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = state.restaurant))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.restaurant.address.town,
                    label = "Town",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                address = state.restaurant.address.copy(
                                    town = it
                                )
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = state.restaurant))
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
                    value = state.restaurant.foodType,
                    label = "Food type",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                foodType = it
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = restaurant))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.restaurant.reviewStars,
                    label = "Stars",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                reviewStars = it
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = restaurant))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.restaurant.phone,
                    label = "Phone",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                phone = it
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = restaurant))
                    }
                )

                Spacer(modifier = spacerModifier)

                LabeledTextField(
                    value = state.restaurant.address.street,
                    label = "Street",
                    onValueChange = {
                        state = state.copy(
                            restaurant = state.restaurant.copy(
                                address = state.restaurant.address.copy(
                                    street = it
                                )
                            )
                        )
                        controller.onEvent(AddModifyRestaurantEvent.FieldEntered(restaurant = restaurant))
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
                        restaurant
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
                        if (newRestaurant) {
                            controller.onEvent(AddModifyRestaurantEvent.CreateRestaurant)
                        } else {
                            controller.onEvent(AddModifyRestaurantEvent.ModifyRestaurant)
                        }
                    }
                )
            }

            Spacer(modifier = spacerModifier)
        }
    }
}