package core.components.restaurants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import core.model.Plate
import core.model.Restaurant

@Composable
fun RestaurantCard(
    modifier: Modifier,
    restaurant: Restaurant,
    canDelete: Boolean = false,
    onDeleteClick: () -> Unit
) {

    val price = calculateRestaurantPrice(restaurant.menu)

    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier
        ){
            // image

            // content
            Row {
                // data
                Column {
                    Text(
                        text = "${restaurant.name} · $price",
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Default.Star, // todo
                            contentDescription = "Star"
                        )
                        Text(
                            text = restaurant.reviewStars,
                        )
                    }
                    Text(
                        text = restaurant.typology
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Telephone"
                        )
                        Text(
                            text = restaurant.phone
                        )
                    }
                }
                // bin icon
                if (canDelete){
                    IconButton(
                        onClick = onDeleteClick
                    ){
                        Icon(
                            imageVector = Icons.Default.Delete, // todo
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }

}

fun calculateRestaurantPrice(menu: List<Plate>): String {
    var totalPrice = 0f

    // calculate average price
    menu.forEach{
        totalPrice += it.price.toFloat()
    }

    val average = totalPrice/menu.size

    // get number of € symbol corresponding to price
    val dollars = if(average in 0f..10f){
        "€"
    } else if(average > 10f && average <= 15f){
        "€€"
    } else if(average > 15f && average <= 20f){
        "€€€"
    } else {
        "€€€€"
    }

    return dollars
}