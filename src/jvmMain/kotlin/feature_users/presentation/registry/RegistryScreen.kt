package feature_users.presentation.registry

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles
import core.components.AppHeader
import core.components.LabeledTextField
import core.components.OneOptionDialog
import core.components.PrimaryButton
import feature_users.presentation.registry.Field.*
import kotlinx.coroutines.flow.collectLatest
import java.awt.Dimension

@Composable
fun RegistryScreen(
    onBack: () -> Unit,
    registryController: RegistryController
) {
    val viewState = registryController.registryState.collectAsState()

    var state by remember { mutableStateOf(RegistryState()) }
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
                        value = state.username,
                        onValueChange = {
                            state = state.copy(
                                username = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, USERNAME))
                        },
                        label = "Username"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.firstname,
                        onValueChange = {
                            state = state.copy(
                                firstname = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, FIRSTNAME))
                        },
                        label = "Name"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.lastname,
                        onValueChange = {
                            state = state.copy(
                                lastname = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, LASTNAME))
                        },
                        label = "Lastname"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.birthDate,
                        onValueChange = {
                            state = state.copy(
                                birthDate = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, BIRTH_DATE))
                        },
                        label = "Birth date"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.street,
                        onValueChange = {
                            state = state.copy(
                                street = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, STREET))
                        },
                        label = "Street"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.town,
                        onValueChange = {
                            state = state.copy(
                                town = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, TOWN))
                        },
                        label = "Town"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.email,
                        onValueChange = {
                            state = state.copy(
                                email = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, EMAIL))
                        },
                        label = "Email"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }

                item {
                    LabeledTextField(
                        value = state.phone,
                        onValueChange = {
                            state = state.copy(
                                phone = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, PHONE))
                        },
                        label = "Phone"
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.password,
                        onValueChange = {
                            state = state.copy(
                                password = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, PASSWORD))
                        },
                        label = "Password",
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = ComeypagaStyles.spacerModifier)
                }
                item {
                    LabeledTextField(
                        value = state.passwordConfirmation,
                        onValueChange = {
                            state = state.copy(
                                passwordConfirmation = it
                            )
                            registryController.onEvent(RegistryEvent.FieldEntered(it, PASSWORD_CONFIRMATION))
                        },
                        label = "Confirm password",
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = ComeypagaStyles.spacerModifier)

                }
                item {
                    PrimaryButton(
                        content = "Create account",
                        onClick = {
                            registryController.onEvent(RegistryEvent.Registry)
                        }
                    )
                }
            }
        }
    }
}