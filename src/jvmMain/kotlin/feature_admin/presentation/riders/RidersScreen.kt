package feature_admin.presentation.riders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.ComeypagaStyles.spacerModifier
import core.Constants
import core.components.dialogs.OneOptionDialog
import core.components.dialogs.TwoOptionDialog
import feature_admin.presentation.riders.components.RiderListItem
import feature_users.domain.model.User
import feature_users.domain.model.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RidersScreen(
    controller: RidersController,
    onAddRider: () -> Unit,
    onClickRider: (UserResponse) -> Unit
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
                RidersController.UiEvent.RiderDeleted -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        // get updated list
                        val result = controller.getRiders()

                        // set result
                        result?.let {
                            riders.value = it
                        }
                    }
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
        title = "Warning",
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
        title = "Warning",
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


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRider,
                shape = CircleShape,
                containerColor = ComeypagaStyles.primaryColorGreen,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add rider"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(riders.value){ rider ->
                Spacer(modifier = spacerModifier)

                // rider item
                RiderListItem(
                    modifier = Modifier
                        .clickable {
                            val appDateFormat = SimpleDateFormat(Constants.APP_DATE)
                            val date = appDateFormat.format(rider.birthDate)

                            onClickRider(UserResponse(
                                _id = rider._id,
                                username = rider.username,
                                firstname = rider.firstname,
                                lastname = rider.lastname,
                                birthDate = date,
                                phone = rider.phone,
                                email = rider.email,
                                address = rider.address,
                                password = rider.password,
                                passwordConfirmation = rider.password,

                            ))
                        },
                    rider = rider,
                    onDeleteRider = {
                        controller.onEvent(RidersEvent.DeleteRider(it))
                    }
                )
            }
        }
    }

}