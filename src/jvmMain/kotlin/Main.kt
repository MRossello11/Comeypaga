import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import core.ComeypagaStyles.appColors
import core.Constants
import core.service.createRetrofit
import feature_user.data.UserRepositoryImpl
import feature_user.data.data_source.UserDataSource
import feature_user.domain.use_cases.LoginUseCase
import feature_user.domain.use_cases.UserUseCases
import feature_user.presentation.login.LoginController
import feature_user.presentation.login.LoginScreen

@Composable
@Preview
fun App() {
    // todo: use dependency injection
    val useCases = UserUseCases(
        loginUseCase = LoginUseCase(UserRepositoryImpl(createRetrofit(Constants.WebService.BASE_URL).create(UserDataSource::class.java)))
    )
    val loginController =
        LoginController(useCases)

    MaterialTheme(
        colors = appColors
    ) {
        LoginScreen(loginController)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        state = WindowState(width = 400.dp, height = 775.dp),
    ) {
        App()
    }
}
