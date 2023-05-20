package core.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.ComeypagaStyles.primaryColorGreen

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    content: String,
    onClick: () -> Unit,
    backgroundColor: Color = primaryColorGreen
){
    Button(
        modifier = modifier,
        onClick = onClick,
        content = {
            Text(text = content)
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            backgroundColor = backgroundColor
        )
    )
}