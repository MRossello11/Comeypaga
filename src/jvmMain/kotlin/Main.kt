import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import core.Constants
import feature_user.presentation.login.LoginController
import feature_user.presentation.login.LoginScreen
import core.service.createRetrofit

@Composable
@Preview
fun App() {
    val loginController = LoginController(createRetrofit(Constants.WebService.BASE_URL))
    MaterialTheme {
        LoginScreen(loginController)
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
