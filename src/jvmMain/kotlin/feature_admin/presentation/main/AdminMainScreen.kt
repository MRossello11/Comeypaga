package feature_admin.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import core.ComeypagaStyles
import core.components.AppHeader
import core.model.Restaurant
import feature_admin.domain.use_cases.AdminUseCases
import feature_admin.presentation.restaurants.AdminRestaurantScreen
import feature_admin.presentation.restaurants.AdminRestaurantsController

@Composable
fun AdminMainScreen(
    adminUseCases: AdminUseCases,
    onBack: () -> Unit,
    onAddRestaurant: () -> Unit,
    onClickRestaurant: (Restaurant) -> Unit
){
    val currentTab = remember { mutableStateOf(0) }

    Column(

    ) {

        AppHeader(
            title = "Administration menu",
            onClickBack = onBack
        )

        TabRow(selectedTabIndex = currentTab.value) {
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 0,
                onClick = { currentTab.value = 0 },
                text = { Text(
                    text = "Restaurants",
                    fontWeight = FontWeight.Bold
                ) },
                selectedContentColor = ComeypagaStyles.primaryColorGreen,
                unselectedContentColor = Color.Gray
            )
            Tab(
                modifier = Modifier.background(ComeypagaStyles.primaryColorGreen),
                selected = currentTab.value == 1,
                onClick = { currentTab.value = 1 },
                text = { Text(
                    text = "Riders",
                    fontWeight = FontWeight.Bold
                ) },
                selectedContentColor = ComeypagaStyles.primaryColorGreen,
                unselectedContentColor = Color.Gray
            )
        }


        when (currentTab.value) {
            0 -> {
                // Content for the "Restaurants" tab
                AdminRestaurantScreen(
                    controller = AdminRestaurantsController(adminUseCases),
                    onAddRestaurant = onAddRestaurant,
                    onClickRestaurant = onClickRestaurant
                )
            }

            1 -> {
                // Content for the "Riders" tab
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "Riders content"
                )
            }
        }
    }
}