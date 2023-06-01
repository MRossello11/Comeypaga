package feature_user.presentation.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.Constants
import core.components.AppHeader
import core.components.PrimaryButton
import core.components.dialogs.OneOptionDialog
import core.components.order.OrderLineListItem
import feature_user.domain.model.OrderLine
import feature_user.presentation.UserOrderController
import feature_user.presentation.UserOrderEvent
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// screen used when viewing details of an order
@Composable
fun OrderDetailsScreen(
    controller: UserOrderController,
    onBack: () -> Unit
){
    val viewState by controller.state.collectAsState()

    // ETA formatter to show in 'HH:mm' format
    val currentDate = LocalDateTime.now()
    val newDate = currentDate.plus(30, ChronoUnit.MINUTES)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    val newFormatted = newDate.format(formatter)

    // number of plates
    val totalPlates = viewState.order.orderLines.sumOf { it.quantity }

    // total price (to check if it's lower than 10€ to add 'commission')
    val totalPrice = viewState.order.orderLines.sumOf { (it.quantity * it.price.toDouble()) }

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
        title = "Warning",
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

                // if order was canceled, go back
                if (viewState.order.state == Constants.OrderStates.CANCELED){
                    onBack()
                }
            }
        )
    }

    Column {
        AppHeader(
            title = "Order details",
            onClickBack = onBack
        )

        // estimated time
        Text(
            text = "Estimated time of arrival: $newFormatted hrs"
        )

        // articles
        Text(
            text = "Articles: $totalPlates",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        LazyColumn {
            // use the stateflow property to ensure UI updates
            items(viewState.order.orderLines){ orderLine ->
                // only show counted order lines
                if (orderLine.quantity > 0) {
                    OrderLineListItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        orderLine = orderLine,
                        quantity = orderLine.quantity,
                        canEdit = false,
                    )
                }
            }
            // < 10E commission
            if (totalPrice < 10){
                item {
                    val lowerThanTenEuroCommission = OrderLine(plateName = "Order lower than 10€", quantity = 1, price = 3f)
                    OrderLineListItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        orderLine = lowerThanTenEuroCommission,
                        quantity = lowerThanTenEuroCommission.quantity,
                        canEdit = false,
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxHeight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row {
                // cancel order
                PrimaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    onClick = {
                        controller.onEvent(UserOrderEvent.CancelOrder)
                    },
                    content = "Cancel order"
                )

                // see ticket
                PrimaryButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp),
                    onClick = {}, // todo see ticket
                    content = "View ticket"
                )
            }
        }
    }
}