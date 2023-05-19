package feature_admin.presentation.riders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.ComeypagaStyles.spacerModifier
import core.components.dialogs.OneOptionDialog
import core.components.dialogs.TwoOptionDialog
import feature_admin.presentation.riders.components.RiderListItem
import feature_users.domain.model.User
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RidersScreen(
    controller: RidersController
) {
    val riders = remember { mutableStateOf(listOf<User>()) }

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var showTwoOptionsDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    // collect UiEvents
    LaunchedEffect(key1 = true) {
        controller.eventFlow.collectLatest { event ->
            when(event){
                is RidersController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }
                is RidersController.UiEvent.ShowDeleteRiderDialogConfirmation -> {
                    errorDialogMessage = event.message
                    showTwoOptionsDialog = true
                }
            }
        }

    }

    // collect riders
    LaunchedEffect(Unit) {
        val result = controller.getRiders()

        // evaluate result
        result?.let {
            if (it.isNotEmpty()) {
                // set riders
                riders.value = it
            } else {
                // didn't find riders (response is ok, but empty)
                showDialog = true
                errorDialogMessage = "No riders found"
            }
        } ?: run {
            // there was an error
            showDialog = true
            errorDialogMessage = "Error getting riders"
        }
    }

    Dialog(
        title = "Aviso",
        visible = showDialog,
        onCloseRequest = {
            showDialog = false
        },
    ) {
        this.window.size = ComeypagaStyles.standardDialogDimension
        OneOptionDialog(
            text = errorDialogMessage,
            onClickButton = {
                showDialog = false
            }
        )
    }

    Dialog(
        title = "Aviso",
        visible = showTwoOptionsDialog,
        onCloseRequest = {
            showTwoOptionsDialog = false
        },
    ) {
        this.window.size = ComeypagaStyles.standardTwoOptionsDialogDimension
        TwoOptionDialog(
            text = errorDialogMessage,
            onClickPositive = {
                // delete rider
                controller.onEvent(RidersEvent.DeletionConfirmed)
                showTwoOptionsDialog = false
            },
            onClickNegative = {
                showTwoOptionsDialog = false
            }
        )
    }


    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(riders.value){ rider ->
                Spacer(modifier = spacerModifier)

                // rider item
                RiderListItem(
                    rider = rider,
                    onDeleteRider = {
                        controller.onEvent(RidersEvent.DeleteRider(it))
                    }
                )
            }
        }
    }

}