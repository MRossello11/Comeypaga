package core.components.restaurants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.model.Plate

@Composable
fun PlateListItem(
    modifier: Modifier = Modifier,
    plate: Plate,
    editMode: Boolean = false,
    onDeletePlate: (Plate) -> Unit
) {
    Column(
        modifier = modifier.defaultMinSize(minHeight = 20.dp)
    ) {
        // name and price
        Text(
            text = "${plate.plateName} · ${plate.price}€"
        )

        // description
        plate.description?.let { description ->
            Text(
                text = description
            )
        }

        if (editMode){
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp),
                onClick = { onDeletePlate(plate) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
        // todo
//        else {
//
//        }
    }
}