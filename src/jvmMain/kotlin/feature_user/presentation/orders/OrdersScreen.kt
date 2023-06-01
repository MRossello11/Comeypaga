package feature_user.presentation.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import core.components.order.OrderListItem
import feature_user.domain.model.Order
import feature_user.presentation.UserOrderController

@Composable
fun OrdersScreen(
    controller: UserOrderController,
    onClickOrder: (Order) -> Unit
) {

    val viewState by controller.state.collectAsState()

    Column {
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