package feature_user.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import core.components.AppHeader

@Composable
fun LoginScreen(
    loginController: LoginController,
) {

    val viewState: LoginState by loginController.loginState.collectAsState()

    var user: String by remember {
        mutableStateOf("")
    }

    var password: String by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        AppHeader("Come y paga")

        Image(
            modifier = Modifier
                .heightIn(30.dp, 60.dp)
                .widthIn(30.dp, 60.dp),
            painter = painterResource("images/ComeYPaga.png"),
            contentDescription = "icon"
        )

        TextField(
            value = user,
            onValueChange = {
                user = it
                loginController.onEvent(LoginEvent.UsernameEntered(it))
            }
        )
        TextField(
            value = password,
            onValueChange = {
                password = it
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