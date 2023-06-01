package feature_user.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import core.ComeypagaStyles
import core.components.AppHeader

var currentTab: MutableState<Int> = mutableStateOf(0)
@Composable
fun UserMainScreen(
    onBack: () -> Unit,
    restaurantsContent: @Composable() () -> Unit,
    cartContent: @Composable() () -> Unit,
    ordersContent: @Composable() () -> Unit,
) {

    Column {
        AppHeader(
            title = when(currentTab.value){
                0 -> "Restaurants"
                1 -> "Cart"
                2 -> "Orders"
                else -> "Come y paga" // shouldn't be seen, but just in case
            },
            onClickBack = onBack
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            when (currentTab.value) {
                // restaurnat screen
                0 -> {
                    restaurantsContent()
                }
                // cart screen
                1 -> {
                    cartContent()
                }
                // order screen
                2 -> {
                    ordersContent()
                }
            }
        }

        TabRow(selectedTabIndex = currentTab.value){
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 0,
                onClick = { currentTab.value = 0 },
                text = {
                    Text(
                        text = "Main",
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home icon"
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 1,
                onClick = { currentTab.value = 1 },
                text = {
                    Text(
                        text = "Cart",
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart icon"
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 2,
                onClick = { currentTab.value = 2 },
                text = {
                    Text(
                        text = "Orders",
                        fontWeight = FontWeight.Bold
                    )
                },
                icon = {
                       Icon(
                           imageVector = Icons.Default.List,
                           contentDescription = "Orders icon"
                       )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
        }
    }
}