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
import feature_users.data.UserRepositoryImpl
import feature_users.data.data_source.UserDataSource
import feature_users.domain.use_cases.LoginUseCase
import feature_users.domain.use_cases.RegistryUseCase
import feature_users.domain.use_cases.ResetPasswordUseCase
import feature_users.domain.use_cases.UserUseCases
import feature_users.presentation.login.LoginController
import feature_users.presentation.login.LoginScreen
import feature_users.presentation.registry.RegistryController
import feature_users.presentation.registry.RegistryScreen
import feature_users.presentation.reset_password.ResetPasswordController
import feature_users.presentation.reset_password.UserResetPasswordScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainContent(){
// todo: use dependency injection
    val repoImpl = UserRepositoryImpl(
        createRetrofit(Constants.WebService.BASE_URL).create(
            UserDataSource::class.java)
    )

    val useCases = UserUseCases(
        loginUseCase = LoginUseCase(repoImpl),
        resetPassword = ResetPasswordUseCase(repoImpl),
        registryUseCase = RegistryUseCase(repoImpl)
    )

    val loginController =
        LoginController(useCases)

    val resetPasswordController = ResetPasswordController(useCases)

    val registryController = RegistryController(useCases)

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
                    },
                    onRegistry = {
                        navigation.push(Screen.Registry)
                    },
                    onUserLogin = {
                        navigation.push(Screen.UserMain(it))
                    },
                    onRiderLogin ={
                        navigation.push(Screen.RiderMain(it))
                    },
                    onAdminLogin = {
                        navigation.push(Screen.AdminMain(it))
                    }
                )
            }

            is Screen.UserResetPassword -> {
                UserResetPasswordScreen(
                    resetPasswordController = resetPasswordController,
                    onBack = navigation::pop
                )
            }

            is Screen.Registry -> {
                RegistryScreen(
                    registryController = registryController,
                    onBack = navigation::pop
                )
            }

            // todo: Users pages
            is Screen.UserMain -> {
                println("User page")
            }
            is Screen.RiderMain -> {
                println("Rider page")
            }
            is Screen.AdminMain ->{
                println("Admin page")
            }
        }
    }
}