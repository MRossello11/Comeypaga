package core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Colors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.awt.Dimension

object ComeypagaStyles {
        val appColors = Colors(
                primary = Color(0xff33cc33),
                onPrimary = Color(0xffffffff),
                primaryVariant = Color(0x2FBC2F),
                secondary = Color(0xff2FBCBC),
                onSecondary = Color(0xffffffff),
                secondaryVariant = Color(0xff07A59B),
                background = Color(0xffffffff),
                onBackground = Color(0xff000000),
                isLight = false,
                error = Color(0xffB00020),
                onError = Color(0xff6e00ee),
                surface = Color(0xffffffff),
                onSurface = Color(0xff000000),
        )

        val primaryColorGreen = Color(0xff33cc33)

        val spacerModifier: Modifier = Modifier.fillMaxWidth().height(20.dp)

        val standardDialogDimension = Dimension(325, 150)
}