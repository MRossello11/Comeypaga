package core.components.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.ComeypagaStyles.appColors
import core.ComeypagaStyles.spacerModifier
import core.components.PrimaryButton

@Composable
fun OneOptionDialog(
    text: String,
    onClickButton: () -> Unit
){
    Column(
        modifier = Modifier
            .background(
                Color.White,
                RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = spacerModifier)

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = spacerModifier)

        PrimaryButton(
            content = "OK",
            onClick = onClickButton
        )
    }
}

@Composable
@Preview
fun PreviewOneOptionDialog(){
    MaterialTheme(appColors) {
        OneOptionDialog(
            text = "Info",
            onClickButton = {}
        )

    }
}