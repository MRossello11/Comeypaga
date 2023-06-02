package feature_rider.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.Constants.OrderStates.DELIVERED_TEXT
import core.Constants.OrderStates.DELIVERING_TEXT
import core.Constants.OrderStates.LATE_TEXT
import core.Utils
import core.components.AppHeader
import core.components.dialogs.OneOptionDialog
import core.components.order.OrderListItem
import kotlinx.coroutines.flow.collectLatest

var currentTab: MutableState<Int> = mutableStateOf(0)
@Composable
fun RiderOrdersScreen(
    controller: RiderController,
    onBack: () -> Unit
) {
    val viewState by controller.state.collectAsState()

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        controller.eventFlow.collectLatest { event ->
            when (event) {
                is RiderController.UiEvent.ShowDialog -> {
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

    Column {
        AppHeader(
            title = "Rider page",
            onClickBack = {
                onBack()
                // stop getting orders
                controller.setGetOrdersMode(false)
            }

        )
        TabRow(selectedTabIndex = currentTab.value) {
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 0,
                onClick = { currentTab.value = 0 },
                text = {
                    Text(
                        text = "All",
                        fontWeight = FontWeight.Bold
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 1,
                onClick = { currentTab.value = 1 },
                text = {
                    Text(
                        text = "My orders",
                        fontWeight = FontWeight.Bold
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
        }

        if (currentTab.value == 0) {
            LazyColumn {
                items(viewState.allOrders) { order ->
                    OrderListItem(
                        order = order,
                        editMode = true,
                        onStateSelected = {
                            controller.updateOrder(order._id!!, Utils.mapStringStateToCode(it))
                        },
                        availableStates = listOf(DELIVERING_TEXT)
                    )
                }
            }
        } else {
            LazyColumn {
                items(viewState.riderOrders) { order ->
                    OrderListItem(
                        order = order,
                        editMode = true,
                        onStateSelected = {
                            controller.updateOrder(order._id!!, Utils.mapStringStateToCode(it))
                        },
                        availableStates = listOf(
                            LATE_TEXT,
                            DELIVERED_TEXT
                        )
                    )
                }
            }
        }
    }
}