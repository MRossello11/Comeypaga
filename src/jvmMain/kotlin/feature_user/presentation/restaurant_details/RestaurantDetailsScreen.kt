package feature_user.presentation.restaurant_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.components.AppHeader
import core.components.dialogs.OneOptionDialog
import core.components.restaurants.PlateListItem
import core.model.Restaurant
import feature_user.presentation.UserOrderController
import feature_user.presentation.UserOrderEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RestaurantDetailsScreen(
    restaurant: Restaurant,
    controller: UserOrderController,
    onBack: () -> Unit,
) {
    val viewState by controller.state.collectAsState()

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        controller.eventFlow.collectLatest { event ->
            when(event){
                is UserOrderController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }
            }
        }
    }

    LaunchedEffect(Unit){
        // update current order
        controller.setOrderInCurse(restaurant)
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
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader(
            title = restaurant.name,
            onClickBack = onBack
        )

        // image
        restaurant.imageBitmap?.let {
            Image(
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 10.dp),
                bitmap = it,
                contentDescription = "Restaurant image",
                contentScale = ContentScale.Fit
            )
        } ?: run {
            Box(
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 10.dp),
            ) {
                Text(text = "Image not found") //todo
            }
        }

        // restaurant name
        Text(
            text = restaurant.name
        )

        // reviews, foodType, typology
        Row {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Reviews star"
            )

            Text(
                text = "${restaurant.reviewStars} · ${restaurant.foodType} · ${restaurant.typology}"
            )
        }

        // telephone and address
        Row {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Restaurant telephone"
            )

            Text(
                text = restaurant.phone
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Address"
            )

            Text(
                text = "${restaurant.address}"
            )
        }

        // menu
        LazyColumn(
            modifier = Modifier
        ) {
            items(viewState.order.orderLines){ orderLine ->
                val plate = restaurant.menu?.find { it._id == orderLine.plateId }
                plate?.let {

                    PlateListItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        plate = plate,
                        onDeletePlate = {},
                        editMode = false,
                        quantity = orderLine.quantity,
                        onChangeQuantity = {
                            controller.onEvent(
                                UserOrderEvent.UpdateOrder(
                                    restaurant = restaurant,
                                    newOrderLine = orderLine.copy(quantity = it)
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
