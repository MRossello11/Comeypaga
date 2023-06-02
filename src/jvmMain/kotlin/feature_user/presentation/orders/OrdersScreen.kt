package feature_user.presentation.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.ComeypagaStyles
import core.components.AppHeader
import core.components.order.OrderListItem
import feature_user.domain.model.Order
import feature_user.presentation.UserOrderController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    controller: UserOrderController,
    onClickOrder: (Order) -> Unit,
    isHistoric: Boolean = false,
    onNavigateToHistoricOrders: () -> Unit = {},
    onBack: () -> Unit = {}
) {

    val viewState by controller.state.collectAsState()

    if (!isHistoric) {
        controller.setGetOrdersUserMode(true)
    }

    Scaffold(
        floatingActionButton = {
            if (!isHistoric) {
                ExtendedFloatingActionButton(
                    containerColor = ComeypagaStyles.primaryColorGreen,
                    contentColor = Color.Black,
                    onClick = onNavigateToHistoricOrders,
                    content = {
                        Text(
                            text = "Historic"
                        )
                    },
                )
            }
        }
    ) {
        Column {
            if (isHistoric) {
                AppHeader(
                    title = "Historic orders",
                    onClickBack = onBack
                )
            }
            LazyColumn {
                items(viewState.ordersInCurse) { orderInCurse ->
                    OrderListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClickOrder(orderInCurse)
                            },
                        order = orderInCurse
                    )
                }
            }
        }
    }
}