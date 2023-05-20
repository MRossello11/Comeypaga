package feature_users.presentation.registry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.components.AppHeader
import core.components.LabeledTextField
import core.components.PrimaryButton
import core.components.dialogs.OneOptionDialog
import feature_users.domain.model.UserResponse
import kotlinx.coroutines.flow.collectLatest
import java.awt.Dimension

@Composable
fun RegistryScreen(
    onBack: () -> Unit,
    registryController: RegistryController,
    user: UserResponse = UserResponse()
) {
    val viewState = registryController.state.collectAsState()

    var state by remember { mutableStateOf(
        RegistryState(
            user = user,
        )
    ) }

    val scrollState = rememberLazyListState()

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        registryController.eventFlow.collectLatest { event ->
            when (event) {
                is RegistryController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }

                RegistryController.UiEvent.Registry -> {
                    errorDialogMessage = viewState.value.registryResponse.message ?: "User registered!"
                    showDialog = true
                }
            }
        }
    }

    Dialog(
        title = "Aviso",
        visible = showDialog,
        onCloseRequest = {
            showDialog = false
        },
    ) {
        this.window.size = Dimension(325, 150)
        OneOptionDialog(
            text = errorDialogMessage,
            onClickButton = {
                showDialog = false

                if(viewState.value.registryResponse.errorCode in 200..299){
                    onBack()
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(
            title = "Registry",
            onClickBack = onBack
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    LabeledTextField(
                        value = state.user.username,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    username = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Username"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.firstname,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    firstname = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Name"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.lastname,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    lastname = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Lastname"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.birthDate,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    birthDate = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Birth date"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.address.street,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    address = state.user.address.copy(
                                        street = it
                                    )
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Street"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.address.town,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    address = state.user.address.copy(
                                        town = it
                                    )
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Town"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.email,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    email = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Email"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }

                item {
                    LabeledTextField(
                        value = state.user.phone,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    phone = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Phone"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.password,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    password = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Password",
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.user.passwordConfirmation,
                        onValueChange = {
                            state = state.copy(
                                user = state.user.copy(
                                    passwordConfirmation = it
                                )
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(state.user))
                        },
                        label = "Confirm password",
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = ComeypagaStyles.spacerModifier)

                }
                item {
                    if (user._id.isEmpty()) {
                        PrimaryButton(
                            content = "Create account",
                            onClick = {
                                registryController.onEvent(RegistryEvent.Registry)
                            }
                        )
                    } else {
                        Row(){
                            PrimaryButton(
                                modifier = Modifier.weight(1f),
                                content = "Cancel",
                                onClick = onBack,
                                backgroundColor = Color.Red
                            )
                            PrimaryButton(
                                modifier = Modifier.weight(1f),
                                content = "Save",
                                onClick = {
                                    registryController.onEvent(RegistryEvent.Modify)
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}