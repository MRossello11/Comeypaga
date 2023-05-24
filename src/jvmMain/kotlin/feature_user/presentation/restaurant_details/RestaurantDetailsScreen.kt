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
import feature_user.domain.model.OrderLine
import feature_user.presentation.UserOrderController
import feature_user.presentation.UserOrderEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RestaurantDetailsScreen(
    restaurant: Restaurant,
    controller: UserOrderController,
    onBack: () -> Unit,
) {
    val viewState = controller.state.collectAsState()

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
            items(restaurant.menu ?: listOf()){ plate ->
//                var quantity by remember { mutableStateOf(0) }
                var quantity by remember { mutableStateOf(
                    try {
                        viewState.value.order.orderLines.filter { it.plateId == plate._id }[0].quantity
                    } catch (_: Exception){
                        0
                    }
                ) }
                
                PlateListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    plate = plate,
                    onDeletePlate = {},
                    editMode = false,
                    initialQuantity = quantity,
                    onChangeQuantity = {
                        val newOrderLine = OrderLine(
                            plateId = plate._id,
                            plateName = plate.plateName,
                            price = plate.price.toFloat(),
                            quantity = it
                        )

                        controller.onEvent(UserOrderEvent.UpdateOrder(
                            newOrderLine = newOrderLine
                        ))
                    }
                )

            }
        }
    }
}
