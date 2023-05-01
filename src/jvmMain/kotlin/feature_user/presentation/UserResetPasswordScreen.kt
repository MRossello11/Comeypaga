package feature_user.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun UserResetPasswordScreen(
    onBack: () -> Unit
){
    Column {
        // todo: this IS a prototype
        Text(text = "TODO: implement")

        Button(
            onClick = onBack,
            content = { Text(text = "Back") }
        )
    }
}