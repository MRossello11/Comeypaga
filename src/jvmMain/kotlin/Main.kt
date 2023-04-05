import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import feature_login.presentation.view.LoginController
import retrofit2.Retrofit
import screens.LoginScreen
import service.createRetrofit

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
