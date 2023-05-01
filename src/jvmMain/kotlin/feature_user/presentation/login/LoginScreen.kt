package feature_user.presentation.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import core.ComeypagaStyles.appColors
import core.ComeypagaStyles.spacerModifier
import core.Constants
import core.components.AppHeader
import core.components.LabeledTextField
import core.components.PrimaryButton
import core.service.createRetrofit
import feature_user.data.UserRepositoryImpl
import feature_user.data.data_source.UserDataSource
import feature_user.domain.use_cases.LoginUseCase
import feature_user.domain.use_cases.UserUseCases

@Composable
fun LoginScreen(
    loginController: LoginController,
    onResetPasswordClick: () -> Unit,
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppHeader(
            title = "Come y paga",
            showBackButton = false,
            onClickBack = {}
        )

        Image(
            modifier = Modifier
                .heightIn(150.dp, 200.dp)
                .widthIn(150.dp, 200.dp),
            painter = painterResource("images/ComeYPaga.png"),
            contentDescription = "icon"
        )

        Spacer(modifier = spacerModifier)

        LabeledTextField(
            value = user,
            onValueChange = {
                user = it
                loginController.onEvent(LoginEvent.UsernameEntered(it))
            },
            label = "Username"
        )

        Spacer(modifier = spacerModifier)

        LabeledTextField(
            value = password,
            onValueChange = {
                password = it
                loginController.onEvent(LoginEvent.PasswordEntered(it))
            },
            label = "Password",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = spacerModifier)

        Text(
            modifier = Modifier.clickable {
                onResetPasswordClick()
            },
            text = "Reset password",
            textDecoration = TextDecoration.Underline,
            color = appColors.primary
        )

        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
            verticalArrangement = Bottom
        ) {
            Spacer(modifier = spacerModifier)

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onResetPasswordClick()
                },
                content = "Create account"
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    loginController.onEvent(LoginEvent.Login)
                },
                content = "Login"
            )
            Spacer(modifier = spacerModifier)
        }
    }
}

@Composable
@Preview
fun PreviewLoginScreen(){
    val useCases = UserUseCases(
        loginUseCase = LoginUseCase(
            UserRepositoryImpl(
                createRetrofit(Constants.WebService.BASE_URL).create(
                    UserDataSource::class.java))
        )
    )
    val loginController =
        LoginController(useCases)

    MaterialTheme(appColors){
        LoginScreen(
            loginController = loginController,
            onResetPasswordClick = {}
        )
    }
}