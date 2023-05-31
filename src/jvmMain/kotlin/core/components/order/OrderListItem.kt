package core.components.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.components.PrimaryButton
import feature_user.domain.model.OrderLine

@Composable
fun OrderListItem(
    modifier: Modifier = Modifier,
    orderLine: OrderLine,
    quantity: Int,
    onChangeQuantity: (Int) -> Unit
) {
    var localQuantity by remember { mutableStateOf(quantity) }
    Row(
        modifier = modifier.defaultMinSize(minHeight = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // name and price
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "${orderLine.plateName} · ${orderLine.price}€"
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            // show - and + buttons (and quantity)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // minus button
                PrimaryButton(
                    modifier = Modifier.clip(CircleShape),
                    content = "-",
                    onClick = {
                        if (localQuantity > 0) { // avoid negative quantities
                            localQuantity--
                            onChangeQuantity(localQuantity)
                        }
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                // quantity text
                Text(
                    text = localQuantity.toString(),
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                PrimaryButton(
                    modifier = Modifier.clip(CircleShape),
                    content = "+",
                    onClick = {
                        localQuantity++
                        onChangeQuantity(localQuantity)
                    }
                )
            }
        }
    }
}