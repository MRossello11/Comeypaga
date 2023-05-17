package core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.FileDialog
import java.awt.Frame
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO

object Utils {
    fun pickPicture(): ImageBitmap? {
        val fileDialog = FileDialog(Frame(), "Select Picture", FileDialog.LOAD)
        fileDialog.file = "*.jpg;*.jpeg;*.png"
        fileDialog.isVisible = true

        val selectedFile = fileDialog.files.firstOrNull()
        return selectedFile?.let { loadImage(it) }?.toComposeImageBitmap()
    }

    private fun loadImage(file: File): BufferedImage? {
        return try {
            ImageIO.read(file)
        } catch (e: Exception) {
            null
        }
    }

    fun imageBitmapToBase64(bitmap: ImageBitmap?): String? {
        if (bitmap == null) return null

        val awtImage = bitmap.toAwtImage()
        val bufferedImage = BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null), BufferedImage.TYPE_INT_ARGB)

        val graphics = bufferedImage.createGraphics()
        graphics.drawImage(awtImage, 0, 0, null)
        graphics.dispose()

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(imageBytes)
    }
}