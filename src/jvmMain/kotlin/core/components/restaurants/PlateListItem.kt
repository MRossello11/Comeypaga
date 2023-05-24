package core.components.restaurants

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.components.PrimaryButton
import core.model.Plate

@Composable
fun PlateListItem(
    modifier: Modifier = Modifier,
    plate: Plate,
    editMode: Boolean = false,
    onDeletePlate: (Plate) -> Unit,
    initialQuantity: Int = 0,
    onChangeQuantity: (Int) -> Unit = {}
) {
    var quantity by remember {
        mutableStateOf(initialQuantity)
    }
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
                text = "${plate.plateName} · ${plate.price}€"
            )

            // description
            plate.description?.let { description ->
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = description
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (editMode) {
                IconButton(
                    onClick = { onDeletePlate(plate) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            } else {
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
                            if (quantity > 0) { // avoid negative quantities
                                quantity--
                                onChangeQuantity(quantity)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // quantity text
                    Text(
                        text = quantity.toString(),
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    PrimaryButton(
                        modifier = Modifier.clip(CircleShape),
                        content = "+",
                        onClick = {
                            quantity++
                            onChangeQuantity(quantity)
                        }
                    )
                }
            }
        }
    }
}