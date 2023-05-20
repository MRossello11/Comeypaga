package core.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import core.ComeypagaStyles

@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: ImageVector? = null,
    onClickTrailingIcon: () -> Unit = {}
){
    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 15.sp
            ),
            label = {
                Text(
                    text = label,
                    fontSize = 10.sp
                )
            },
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            trailingIcon = {
                trailingIcon?.let {
                    IconButton(
                        onClick = onClickTrailingIcon
                    ) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = "Trailing icon"
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview
fun LabeledTextFieldPreview(){
    MaterialTheme(
        colors = ComeypagaStyles.appColors
    ) {
        LabeledTextField(
            value = "Migue",
            onValueChange = {},
            label = "Username"
        )
    }
}