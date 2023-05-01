import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import core.ComeypagaStyles.appColors
import core.navigation.ProvideComponentContext

@Composable
@Preview
fun App() {
    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    MaterialTheme(
        colors = appColors
    ) {
        CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()){
            ProvideComponentContext(rootComponentContext){
                MainContent()
            }
        }

    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        state = WindowState(width = 400.dp, height = 775.dp),
    ) {
        App()
    }
}
