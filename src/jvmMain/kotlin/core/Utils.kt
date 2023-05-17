package core

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.FileDialog
import java.awt.Frame
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Utils {
    fun getImageBitmapFromFile(file: File, selectedPicture: MutableState<ImageBitmap?>){
        selectedPicture.value = loadImage(file)?.toComposeImageBitmap()
    }
    fun pickPicture(selectedPicture: MutableState<ImageBitmap?>) {
        val fileDialog = FileDialog(Frame(), "Select Picture", FileDialog.LOAD)
        fileDialog.file = "*.jpg;*.jpeg;*.png"
        fileDialog.isVisible = true

        val selectedFile = fileDialog.files.firstOrNull()
        selectedPicture.value = selectedFile?.let { loadImage(it) }?.toComposeImageBitmap()
    }

    private fun loadImage(file: File): BufferedImage? {
        return try {
            ImageIO.read(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}