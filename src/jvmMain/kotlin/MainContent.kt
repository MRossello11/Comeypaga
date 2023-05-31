import androidx.compose.material3.Text
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
import core.model.Plate
import core.model.Restaurant
import core.navigation.ChildStack
import core.navigation.Screen
import core.service.createRetrofit
import feature_admin.data.AdminRepositoryImpl
import feature_admin.data.data_source.AdminDataSource
import feature_admin.domain.use_cases.*
import feature_admin.presentation.add_modify_plate.AddModifyPlateController
import feature_admin.presentation.add_modify_plate.AddModifyPlateScreen
import feature_admin.presentation.add_modify_restaurant.AddModifyRestaurantController
import feature_admin.presentation.add_modify_restaurant.AddModifyRestaurantScreen
import feature_admin.presentation.main.AdminMainScreen
import feature_admin.presentation.menu.MenuController
import feature_admin.presentation.menu.MenuScreen
import feature_admin.presentation.restaurants.AdminRestaurantsController
import feature_admin.presentation.restaurants.RestaurantsScreen
import feature_admin.presentation.riders.RidersController
import feature_admin.presentation.riders.RidersScreen
import feature_user.data.UserOrderRepositoryImpl
import feature_user.data.data_source.UserOrderDataSource
import feature_user.domain.use_cases.CancelOrder
import feature_user.domain.use_cases.GetOrdersUser
import feature_user.domain.use_cases.UpdateOrder
import feature_user.domain.use_cases.UserOrderUseCases
import feature_user.presentation.UserOrderController
import feature_user.presentation.cart.CartScreen
import feature_user.presentation.main.UserMainScreen
import feature_user.presentation.restaurant_details.RestaurantDetailsScreen
import feature_users.data.UserRepositoryImpl
import feature_users.data.data_source.UserDataSource
import feature_users.domain.model.Role
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

    val userRepository = UserRepositoryImpl(
        retrofit.create(
            UserDataSource::class.java)
    )

    val userUseCases = UserUseCases(
        loginUseCase = LoginUseCase(userRepository),
        resetPassword = ResetPasswordUseCase(userRepository),
        registryUseCase = RegistryUseCase(userRepository)
    )

    val loginController =
        LoginController(userUseCases)

    val resetPasswordController = ResetPasswordController(userUseCases)


    // admin
    val adminRepository = AdminRepositoryImpl(
        retrofit.create(
            AdminDataSource::class.java)
    )

    val adminUseCases = AdminUseCases(
        getRestaurant = GetRestaurant(adminRepository),
        getRestaurants = GetRestaurants(adminRepository),
        addRestaurant = AddRestaurant(adminRepository),
        deleteRestaurant = DeleteRestaurant(adminRepository),
        addPlate = AddPlate(adminRepository),
        modifyPlate = ModifyPlate(adminRepository),
        deletePlate = DeletePlate(adminRepository),
        getRiders = GetRiders(adminRepository),
        postRider = PostRider(adminRepository),
        deleteRider = DeleteRider(adminRepository),
    )

    var actualRestaurant: Restaurant

    // user order
    val userOrderRepository = UserOrderRepositoryImpl(retrofit.create(UserOrderDataSource::class.java))
    val userOrderUseCases = UserOrderUseCases(
        cancelOrder = CancelOrder(userOrderRepository),
        updateOrder = UpdateOrder(userOrderRepository),
        getOrdersUser = GetOrdersUser(userOrderRepository)
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
                        navigation.push(Screen.Registry(Role.User))
                    },
                    onUserLogin = {
                        navigation.push(Screen.UserMain(it))
                    },
                    onRiderLogin = {
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
                    registryController = RegistryController(
                        userUseCases = userUseCases,
                        userRole = screen.userRole,
                        user = screen.user,
                        postRider = PostRider(adminRepository)
                    ),
                    user = screen.user,
                    onBack = navigation::pop
                )
            }

            is Screen.RiderMain -> {
                println("Rider page")
            }

            is Screen.AdminMain -> {
                AdminMainScreen(
                    onBack = navigation::pop,
                    restaurantsContent = {
                        val adminRestaurantsController = AdminRestaurantsController(adminUseCases)
                        RestaurantsScreen(
                            controller = adminRestaurantsController,
                            onAddRestaurant = {
                                navigation.push(Screen.AddModifyRestaurant(null))
                            },
                            onClickRestaurant = {
                                navigation.push(Screen.AddModifyRestaurant(it))
                            },
                            editMode = true
                        )
                    },
                    ridersContent = {
                        val ridersController = RidersController(adminUseCases)
                        RidersScreen(
                            controller = ridersController,
                            onAddRider = {
                                navigation.push(Screen.Registry(Role.Rider))
                            },
                            onClickRider = {
                                navigation.push(Screen.Registry(Role.Rider, it))
                            }
                        )
                    }
                )
            }

            is Screen.AddModifyRestaurant -> {
                screen.restaurant?.let { restaurant ->
                    actualRestaurant = restaurant
                    AddModifyRestaurantScreen(
                        controller = AddModifyRestaurantController(adminUseCases, actualRestaurant),
                        restaurant = restaurant,
                        onBack = navigation::pop,
                        onNavigateToMenu = {
                            navigation.push(Screen.MenuScreen(it))
                        },
                        newRestaurant = screen.restaurant._id?.let { false } ?: true
                    )
                } ?: run {
                    AddModifyRestaurantScreen(
                        controller = AddModifyRestaurantController(adminUseCases),
                        onBack = navigation::pop,
                        onNavigateToMenu = {
                            navigation.push(Screen.MenuScreen(it))
                        },
                        newRestaurant = true
                    )
                }
            }

            is Screen.MenuScreen -> {
                MenuScreen(
                    restaurant = screen.restaurant,
                    controller = MenuController(
                        adminUseCases = adminUseCases,
                        restaurant = screen.restaurant
                    ),
                    onBack = navigation::pop,
                    onClickAddPlate = {
                        navigation.push(Screen.AddModifyPlateScreen(it, null))
                    },
                    onClickPlate = { restaurant: Restaurant, plate: Plate ->
                        navigation.push(Screen.AddModifyPlateScreen(restaurant, plate))
                    }
                )
            }

            is Screen.AddModifyPlateScreen -> {
                val addModifyPlateController = AddModifyPlateController(
                    adminUseCases = adminUseCases
                )

                screen.restaurant?.let { restaurant ->
                    screen.plate?.let { plate ->
                        AddModifyPlateScreen(
                            controller = addModifyPlateController,
                            restaurant = restaurant,
                            plate = plate,
                            onBack = navigation::pop,
                            newPlate = false
                        )
                    } ?: run {
                        AddModifyPlateScreen(
                            controller = addModifyPlateController,
                            restaurant = restaurant,
                            onBack = navigation::pop,
                            newPlate = true
                        )
                    }
                }
            }
            // User pages
            is Screen.UserMain -> {
                UserMainScreen(
                    onBack = navigation::pop,
                    restaurantsContent = {
                        RestaurantsScreen(
                            controller = AdminRestaurantsController(adminUseCases),
                            onAddRestaurant = {},
                            onClickRestaurant = {
                                // navigate to RestaurantDetails
                                navigation.push(Screen.RestaurantDetailsScreen(it))
                            },
                        )
                    },
                    cartContent = {
                        CartScreen(
                            controller = UserOrderController(userOrderUseCases),
                            onNavigateToCheckout = {} // todo
                        )
                    },
                    ordersContent = {
                        Text(text = "Orders content")
                    }
                )
            }

            // user order
            is Screen.RestaurantDetailsScreen -> {
                RestaurantDetailsScreen(
                    restaurant = screen.restaurant,
                    controller = UserOrderController(userOrderUseCases),
                    onBack = navigation::pop
                )
            }
        }
    }
}