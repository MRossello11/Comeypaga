package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.components.AppHeader
import feature_login.presentation.login.LoginController
import feature_login.presentation.login.LoginEvent
import feature_login.presentation.login.LoginState

@Composable
fun LoginScreen(
    loginController: LoginController,
) {

    val viewState: LoginState by loginController.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        AppHeader("Come Y paga")

        Image(
            modifier = Modifier
                .heightIn(30.dp, 60.dp)
                .widthIn(30.dp, 60.dp),
            painter = painterResource("images/ComeYPaga.png"),
            contentDescription = "icon"
        )

        TextField(
            value = viewState.username,
            onValueChange = {
                loginController.onEvent(LoginEvent.UsernameEntered(it))
            }
        )
        TextField(
            value = viewState.password,
            onValueChange = {
                loginController.onEvent(LoginEvent.PasswordEntered(it))
            }
        )

        Button(
            onClick = {
                loginController.onEvent(LoginEvent.Login)
            },
            content = {
                Text(text = "Login")
            }
        )

        Text(
            text = "Return code: ${viewState.wsReturnCode}"
        )
    }
}