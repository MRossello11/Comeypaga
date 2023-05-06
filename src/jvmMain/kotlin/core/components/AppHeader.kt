package core.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.ComeypagaStyles.appColors
import core.ComeypagaStyles.primaryColorGreen

@Composable
fun AppHeader(
    title: String,
    showBackButton: Boolean = true,
    onClickBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = primaryColorGreen)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (showBackButton) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = onClickBack,
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back arrow",
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewAppHeader(){
    MaterialTheme(appColors) {
        AppHeader(
            title = "Reset password",
            onClickBack = {}
        )
    }
}