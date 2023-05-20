package feature_admin.presentation.riders.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import feature_users.domain.model.User

@Composable
fun RiderListItem(
    modifier: Modifier = Modifier,
    rider: User,
    onDeleteRider: (User) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        // rider data
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = rider.username
            )

            Text(
                text = "${rider.firstname} ${rider.lastname}"
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ){
            // delete icon
            IconButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp),
                onClick = {
                    onDeleteRider(rider)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete rider"
                )
            }
        }
    }

}

