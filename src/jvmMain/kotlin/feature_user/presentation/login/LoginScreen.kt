package feature_user.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.ComeypagaStyles.appColors
import core.ComeypagaStyles.spacerModifier
import core.components.AppHeader
import core.components.LabeledTextField
import core.components.OneOptionDialog
import core.components.PrimaryButton
import kotlinx.coroutines.flow.collectLatest
import java.awt.Dimension

@Composable
fun LoginScreen(
    loginController: LoginController,
    onResetPasswordClick: () -> Unit,
    onRegistry: () -> Unit,
) {

    val viewState: LoginState by loginController.loginState.collectAsState()

    var user: String by remember {
        mutableStateOf("")
    }

    var password: String by remember {
        mutableStateOf("")
    }

    // dialog states
    var showDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        loginController.eventFlow.collectLatest { event ->
            when (event) {
                is LoginController.UiEvent.ShowDialog -> {
                    errorDialogMessage = event.message
                    showDialog = true
                }

                LoginController.UiEvent.Login -> TODO()
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
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppHeader(
            title = "Come y paga",
            showBackButton = false,
            onClickBack = {}
        )

        Image(
            modifier = Modifier
                .heightIn(150.dp, 200.dp)
                .widthIn(150.dp, 200.dp),
            painter = painterResource("images/ComeYPaga.png"),
            contentDescription = "icon"
        )

        Spacer(modifier = spacerModifier)

        LabeledTextField(
            value = user,
            onValueChange = {
                user = it
                loginController.onEvent(LoginEvent.UsernameEntered(it))
            },
            label = "Username"
        )

        Spacer(modifier = spacerModifier)

        LabeledTextField(
            value = password,
            onValueChange = {
                password = it
                loginController.onEvent(LoginEvent.PasswordEntered(it))
            },
            label = "Password",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = spacerModifier)

        Text(
            modifier = Modifier.clickable {
                onResetPasswordClick()
            },
            text = "Reset password",
            textDecoration = TextDecoration.Underline,
            color = appColors.primary
        )

        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
            verticalArrangement = Bottom
        ) {
            Spacer(modifier = spacerModifier)

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onRegistry()
                },
                content = "Create account"
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    loginController.onEvent(LoginEvent.Login)
                },
                content = "Login"
            )
            Spacer(modifier = spacerModifier)
        }
    }
}