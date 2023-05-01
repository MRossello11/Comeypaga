import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import core.Constants
import feature_user.presentation.login.LoginController
import feature_user.presentation.login.LoginScreen
import core.service.createRetrofit
import feature_user.data.UserRepositoryImpl
import feature_user.data.data_source.UserDataSource
import feature_user.domain.use_cases.LoginUseCase
import feature_user.domain.use_cases.UserUseCases

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
        colors = Colors(
            primary = Color(0xff33cc33),
            onPrimary = Color(0xffffffff),
            primaryVariant = Color(0x2FBC2F),
            secondary = Color(0xff2FBCBC),
            onSecondary = Color(0xffffffff),
            secondaryVariant = Color(0xff07A59B),
            background = Color(0xffffffff),
            onBackground = Color(0xff000000),
            isLight = false,
            error = Color(0xffB00020),
            onError = Color(0xff6e00ee),
            surface = Color(0xffffffff),
            onSurface = Color(0xff000000),
        )
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
