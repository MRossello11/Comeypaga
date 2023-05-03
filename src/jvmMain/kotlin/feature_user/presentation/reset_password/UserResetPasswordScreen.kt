package feature_user.presentation.reset_password

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
import core.components.OneOptionDialog
import core.components.PrimaryButton
import java.awt.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserResetPasswordScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    resetPasswordController: ResetPasswordController
) {
    val viewState: State<ResetPasswordState> = resetPasswordController.resetPasswordState.collectAsState()
    var resetPasswordState by remember { mutableStateOf(ResetPasswordState()) }

    Dialog(
        title = "Aviso",
        visible = !viewState.value.responseEventConsumed,
        onCloseRequest = resetPasswordController::consumeResponseEvent,
    ) {
        this.window.size = Dimension(325, 150)
        OneOptionDialog(
            text = viewState.value.resetPasswordResponse.message ?: "",
            onClickButton = {
                resetPasswordController.consumeResponseEvent()

                // go back if response is ok
                if (viewState.value.resetPasswordResponse.errorCode in 200..299){
                    onBack()
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
                value = resetPasswordState.username,
                onValueChange = {
                    resetPasswordState = resetPasswordState.copy(
                        username = it
                    )
                    resetPasswordController.onEvent(ResetPasswordEvent.FieldEntered(it, ResetPasswordField.USERNAME))
                },
                label = "Username"
            )

            Spacer(modifier = spacerModifier)

            LabeledTextField(
                value = resetPasswordState.password,
                onValueChange = {
                    resetPasswordState = resetPasswordState.copy(
                        password = it
                    )
                    resetPasswordController.onEvent(ResetPasswordEvent.FieldEntered(it, ResetPasswordField.PASSWORD))
                },
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = spacerModifier)

            LabeledTextField(
                value = resetPasswordState.passwordConfirmation,
                onValueChange = {
                    resetPasswordState = resetPasswordState.copy(
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