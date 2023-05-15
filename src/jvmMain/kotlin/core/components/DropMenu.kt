package core.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.runtime.*
import core.ComeypagaStyles

@Composable
fun DropMenu(
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }
    ){
        TextField(
            value = "Type",
            onValueChange = {},
            readOnly = true
        )

        items.forEach {
            DropdownMenuItem(
                onClick = {
                    onItemClick(it)
                },
            ) {
                Text(
                    text = it
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropMenu(){
    MaterialTheme(
        colors = ComeypagaStyles.appColors
    ){
        DropMenu(
            items = listOf("starter", "main", "drink", "dessert"),
            onItemClick = {}
        )
    }
}