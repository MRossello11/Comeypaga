package feature_user.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.components.PrimaryButton
import core.components.dialogs.OneOptionDialog
import core.components.order.OrderLineListItem
import feature_user.domain.model.Order
import feature_user.domain.model.OrderLine
import feature_user.presentation.UserOrderController
import feature_user.presentation.UserOrderEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CartScreen(
    controller: UserOrderController,
    onNavigateToCheckout: (Order) -> Unit
) {
    val viewState by controller.state.collectAsState()

    val totalPlates = viewState.order.orderLines.sumOf { it.quantity }
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
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        // articles
        Text(
            text = "Articles: $totalPlates",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        if (totalPlates > 0) {

            LazyColumn {
                // use the stateflow property to ensure UI updates
                items(viewState.order.orderLines) { orderLine ->
                    // only show counted order lines
                    if (orderLine.quantity > 0) {
                        OrderLineListItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            orderLine = orderLine,
                            quantity = orderLine.quantity,
                            onChangeQuantity = {
                                controller.onEvent(UserOrderEvent.UpdateOrder(newOrderLine = orderLine.copy(quantity = it)))
                            }
                        )
                    }
                }
                // < 10E commission
                if (totalPrice < 10) {
                    item {
                        val lowerThanTenEuroCommission =
                            OrderLine(plateName = "Order lower than 10€", quantity = 1, price = 3f)
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


            // green line separator
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .background(ComeypagaStyles.primaryColorGreen)
            )

            // subtotal
            Row {
                // subtotal text
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Subtotal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // subtotal price
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${String.format("%.2f", if (totalPrice < 10) totalPrice + 3 else totalPrice)}€",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                // checkout button
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    onClick = {
                        controller.onEvent(UserOrderEvent.SendOrder)
                        onNavigateToCheckout(viewState.order)
                    },
                    content = "Confirm order (${
                        String.format(
                            "%.2f",
                            if (totalPrice < 10) totalPrice + 3 else totalPrice
                        )
                    }€)"
                )
            }
        } else {
            Text(
                text = "No order in curse"
            )
        }
    }
}