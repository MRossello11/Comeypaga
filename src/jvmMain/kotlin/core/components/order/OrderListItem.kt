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
import core.Constants.OrderStates.CANCELED
import core.Constants.OrderStates.CANCELED_TEXT
import core.Constants.OrderStates.CREATED
import core.Constants.OrderStates.CREATED_TEXT
import core.Constants.OrderStates.DELIVERING
import core.Constants.OrderStates.DELIVERING_TEXT
import core.Constants.OrderStates.IN_PROGRESS
import core.Constants.OrderStates.IN_PROGRESS_TEXT
import core.Constants.OrderStates.LATE
import core.Constants.OrderStates.LATE_TEXT
import core.components.DropMenu
import feature_user.domain.model.Order
import java.text.SimpleDateFormat

@Composable
fun OrderListItem(
    modifier: Modifier = Modifier,
    order: Order,
    editMode: Boolean = false, // true for riders as they can edit the state
    onStateSelected: (String) -> Unit = {} // when a rider selects a state
) {
    val stateText = when(order.state){
        CREATED -> {
            CREATED_TEXT
        }
        IN_PROGRESS -> {
            IN_PROGRESS_TEXT
        }
        DELIVERING -> {
            DELIVERING_TEXT
        }
        LATE -> {
            LATE_TEXT
        }
        CANCELED ->{
            CANCELED_TEXT
        }
        else ->{
            "Unknown"
        }
    }

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
                items = listOf(DELIVERING_TEXT, LATE_TEXT),
                onItemClick = {
                    onStateSelected(it)
                },
                selectedItem = ""
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