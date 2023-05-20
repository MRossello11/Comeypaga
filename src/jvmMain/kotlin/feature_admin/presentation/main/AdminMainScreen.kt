package feature_admin.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.TabRow
import androidx.compose.material.Text
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
fun AdminMainScreen(
    onBack: () -> Unit,
    restaurantsContent: @Composable() () -> Unit,
    ridersContent: @Composable() () -> Unit
){
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
                restaurantsContent()
            }

            1 -> {
                // Content for the "Riders" tab
                ridersContent()
            }
        }
    }
}