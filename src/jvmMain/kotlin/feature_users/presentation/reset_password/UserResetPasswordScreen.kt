package feature_users.presentation.reset_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles.spacerModifier
import core.components.AppHeader
import core.components.LabeledTextField
import core.components.PrimaryButton
import core.components.dialogs.OneOptionDialog
import kotlinx.coroutines.flow.collectLatest
import java.awt.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserResetPasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    resetPasswordController: ResetPasswordController
) {
    // controller form state
    val viewState: State<ResetPasswordState> = resetPasswordController.resetPasswordState.collectAsState()

    // form state
    var resetPasswordFormState by remember { mutableStateOf(ResetPasswordState()) }

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true){
        resetPasswordController.eventFlow.collectLatest { event ->
            when(event){
                is ResetPasswordController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }
            }
        }
    }

    Dialog(
        title = "Warning",
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

                // go back if response is ok
                viewState.value.resetPasswordResponse.errorCode?.let { errorCode ->
                    if (errorCode in 200..299){
                        onBack()
                    }
                }
            }
        )
    }

    Scaffold(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppHeader("Reset password") { onBack() }

            Spacer(modifier = spacerModifier)

            Text(
                text = "Enter your username and a new password"
            )

            Spacer(modifier = spacerModifier)

            LabeledTextField(
                value = resetPasswordFormState.username,
                onValueChange = {
                    resetPasswordFormState = resetPasswordFormState.copy(
                        username = it
                    )
                    resetPasswordController.onEvent(ResetPasswordEvent.FieldEntered(it, ResetPasswordField.USERNAME))
                },
                label = "Username"
            )

            Spacer(modifier = spacerModifier)

            LabeledTextField(
                value = resetPasswordFormState.password,
                onValueChange = {
                    resetPasswordFormState = resetPasswordFormState.copy(
                        password = it
                    )
                    resetPasswordController.onEvent(ResetPasswordEvent.FieldEntered(it, ResetPasswordField.PASSWORD))
                },
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = spacerModifier)

            LabeledTextField(
                value = resetPasswordFormState.passwordConfirmation,
                onValueChange = {
                    resetPasswordFormState = resetPasswordFormState.copy(
                        passwordConfirmation = it
                    )
                    resetPasswordController.onEvent(
                        ResetPasswordEvent.FieldEntered(
                            it,
                            ResetPasswordField.PASSWORD_CONFIRMATION
                        )
                    )
                },
                label = "Confirm password",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = spacerModifier)

            PrimaryButton(
                content = "Reset password",
                onClick = {
                    resetPasswordController.onEvent(ResetPasswordEvent.ResetPassword)
                }
            )
        }
    }
}