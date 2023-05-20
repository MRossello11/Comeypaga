package core.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import core.ComeypagaStyles

@Composable
fun DropMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    onItemClick: (String) -> Unit,
    selectedItem: String
) {
    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }
    var currentItem by remember {
        mutableStateOf(selectedItem)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LabeledTextField(
                modifier = Modifier
                    .clickable {
                        expanded = !expanded
                    }
                    .onFocusChanged {
                        expanded = it.hasFocus
                    },
                value = currentItem,
                onValueChange = {},
                readOnly = true,
                label = "Type",
                trailingIcon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                onClickTrailingIcon = {
                    expanded = !expanded
                }
            )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        currentItem = it
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
}

@Preview
@Composable
fun PreviewDropMenu(){
    MaterialTheme(
        colors = ComeypagaStyles.appColors
    ){
        DropMenu(
            modifier = Modifier.fillMaxWidth(),
            items = listOf("starter", "main", "drink", "dessert"),
            onItemClick = {},
            selectedItem = "Type"
        )
    }
}