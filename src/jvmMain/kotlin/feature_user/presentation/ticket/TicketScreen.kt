package feature_user.presentation.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.Properties
import core.components.AppHeader
import java.text.SimpleDateFormat

@Composable
fun TicketScreen(
    controller: TicketController,
    onBack: () -> Unit
) {
    val viewState by controller.state.collectAsState()

    val totalPrice = viewState.order.orderLines.sumOf { (it.quantity * it.price.toDouble()) }

    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val arrivalTimeFormatted = formatter.format(viewState.order.arrivalTime)

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        AppHeader(
            title = "Ticket",
            onClickBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {


            // restaurant data
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Come y paga",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Text(
                    text = viewState.restaurant.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    text = viewState.restaurant.address.street,
                )

                Text(
                    text = viewState.restaurant.address.town,
                )

                if (viewState.restaurant.phone.isNotEmpty()) {
                    Text(
                        text = "TEL: ${viewState.restaurant.phone}"
                    )
                }

                if (viewState.restaurant.email.isNotEmpty()) {
                    Text(
                        text = "E-MAIL: ${viewState.restaurant.email}"
                    )
                }
            }

            // arrival time
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = arrivalTimeFormatted
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )

            // client data
            Text(
                text = "Client",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "User: ${Properties.userLogged?.username}"
            )

            Text(
                text = "Shipping address: ${viewState.order.shippingAddress}"
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )

            // plates
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn {
                    items(viewState.order.orderLines) { orderLine ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "${orderLine.quantity} ${orderLine.plateName}"
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "${orderLine.price}"
                                )
                            }
                        }
                    }

                    // < 10E commission
                    if (totalPrice < 10){
                        item {
                            Row {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "Order lower than 10€"
                                    )
                                }
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "3"
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "Total ${String.format("%.2f", if (totalPrice < 10) totalPrice + 3 else totalPrice)}",
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Thank you for ordering in Come y paga"
                )
            }
        }
    }
}