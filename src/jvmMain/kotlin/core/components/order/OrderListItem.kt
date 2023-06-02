package core.components.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.Utils
import core.components.DropMenu
import feature_user.domain.model.Order
import java.text.SimpleDateFormat

@Composable
fun OrderListItem(
    modifier: Modifier = Modifier,
    order: Order,
    editMode: Boolean = false, // true for riders as they can edit the state
    onStateSelected: (String) -> Unit = {}, // when a rider selects a state
    availableStates: List<String> = listOf()
) {
    val stateText = Utils.mapStateCodeToString(order.state)

    val formatter = SimpleDateFormat("HH:mm")
    val currentFormatted = formatter.format(order.arrivalTime)

    Column(
        modifier = modifier
    ) {
        Text(
            text = order.restaurantName,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Articles: ${order.orderLines.sumOf { it.quantity }}"
        )

        if (editMode){
            DropMenu(
                items = availableStates,
                onItemClick = {
                    onStateSelected(it)
                },
                selectedItem = Utils.mapStateCodeToString(order.state)
            )

        } else {
            Text(
                text = "State: $stateText"
            )
        }

        // estimated time
        Text(
            text = "Estimated time of arrival: $currentFormatted hrs"
        )
        Spacer(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color.Black))
    }
}