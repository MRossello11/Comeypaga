import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import core.Constants
import core.navigation.ChildStack
import core.navigation.Screen
import core.service.createRetrofit
import feature_user.data.UserRepositoryImpl
import feature_user.data.data_source.UserDataSource
import feature_user.domain.use_cases.LoginUseCase
import feature_user.domain.use_cases.UserUseCases
import feature_user.presentation.UserResetPasswordScreen
import feature_user.presentation.login.LoginController
import feature_user.presentation.login.LoginScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainContent(){
// todo: use dependency injection
    val useCases = UserUseCases(
        loginUseCase = LoginUseCase(
            UserRepositoryImpl(
                createRetrofit(Constants.WebService.BASE_URL).create(
                    UserDataSource::class.java))
        )
    )
    val loginController =
        LoginController(useCases)

    // navigation
    val navigation = remember { StackNavigation<Screen>() }
    ChildStack(
        source = navigation,
        initialStack = { listOf(Screen.Login) },
        animation = stackAnimation(fade() + scale()),
    ) { screen ->
        when (screen) {
            is Screen.Login -> {
                LoginScreen(
                    loginController = loginController,
                    onResetPasswordClick = {
                        navigation.push(Screen.UserResetPassword)
                    }
                )
            }

            is Screen.UserResetPassword -> {
                UserResetPasswordScreen(
                    onBack = navigation::pop
                )
            }

        }
    }
}