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
import feature_admin.data.AdminRepositoryImpl
import feature_admin.data.data_source.AdminDataSource
import feature_admin.domain.use_cases.*
import feature_admin.presentation.add_modify_restaurant.AddModifyRestaurantScreen
import feature_admin.presentation.add_modify_restaurant.AddRestaurantController
import feature_admin.presentation.menu.MenuController
import feature_admin.presentation.menu.MenuScreen
import feature_admin.presentation.restaurants.AdminRestaurantScreen
import feature_admin.presentation.restaurants.AdminRestaurantsController
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
    val retrofit =
        createRetrofit(Constants.WebService.BASE_URL)

    val repoImpl = UserRepositoryImpl(
        retrofit.create(
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

    // admin
    // todo: until rider screen is finished
    val adminRepository = AdminRepositoryImpl(
        retrofit.create(
            AdminDataSource::class.java)
    )

    val adminUseCases = AdminUseCases(
        getRestaurants = GetRestaurants(adminRepository),
        addRestaurant = AddRestaurant(adminRepository),
        modifyRestaurant = ModifyRestaurant(adminRepository),
        deleteRestaurant = DeleteRestaurant(adminRepository),
        addPlate = AddPlate(adminRepository),
        modifyPlate = ModifyPlate(adminRepository),
        deletePlate = DeletePlate(adminRepository),
    )

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

                val adminRestaurantsController = AdminRestaurantsController(adminUseCases)
                AdminRestaurantScreen(
                    controller = adminRestaurantsController,
                    onBack = navigation::pop,
                    onAddRestaurant = {
                        navigation.push(Screen.AddModifyRestaurant(null))
                    },
                    onClickRestaurant = {
                        navigation.push(Screen.AddModifyRestaurant(it))
                    }
                )
            }
            is Screen.AddModifyRestaurant -> {
                AddModifyRestaurantScreen(
                    controller = AddRestaurantController(adminUseCases),
                    restaurant = screen.restaurant,
                    onBack = navigation::pop,
                    onClickEditMenu = {
                        navigation.push(Screen.MenuScreen(it))
                    }
                )
            }
            is Screen.MenuScreen -> {
                val controller = MenuController(
                    adminUseCases = adminUseCases
                )
                MenuScreen(
                    restaurant = screen.restaurant,
                    controller = controller,
                    onBack = navigation::pop,
                    onClickAddPlate = { /* TODO: navigate to add plate screen */},
                    onClickPlate = { /* TODO: navigate to modify plate screen */ }
                )
            }
        }
    }
}