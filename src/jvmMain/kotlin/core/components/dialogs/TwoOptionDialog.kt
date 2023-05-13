package core.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import core.ComeypagaStyles
import core.components.PrimaryButton

@Composable
fun TwoOptionDialog(
    text: String,
    onClickPositive: () -> Unit,
    onClickNegative: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                Color.White,
                RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = ComeypagaStyles.spacerModifier)

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))

        Row {
            PrimaryButton(
                modifier = Modifier
                    .weight(1f)
                    .height(35.dp)
                    .padding(horizontal = 10.dp),
                content = "Cancel",
                onClick = onClickNegative,
                backgroundColor = Color.Red
            )
            PrimaryButton(
                modifier = Modifier
                    .weight(1f)
                    .height(35.dp)
                    .padding(horizontal = 10.dp),
                content = "OK",
                onClick = onClickPositive
            )
        }

        Spacer(modifier = ComeypagaStyles.spacerModifier)
    }
}