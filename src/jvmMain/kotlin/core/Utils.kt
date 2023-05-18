package core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import feature_users.domain.model.InvalidUser
import feature_users.domain.model.UserResponse
import java.awt.FileDialog
import java.awt.Frame
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
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


    @Throws(InvalidUser::class)
    fun verifyUser(user: UserResponse) {
        // verify fields
        if (user.username.isEmpty()) {
            throw InvalidUser("Username cannot be empty")
        }
        if (user.firstname.isEmpty()) {
            throw InvalidUser("Firstname cannot be empty")
        }
        if (user.lastname.isEmpty()) {
            throw InvalidUser("Lastname cannot be empty")
        }
        if (user.birthDate.isEmpty()) {
            throw InvalidUser("Birth date cannot be empty")
        }
        if (user.address.street.isEmpty()) {
            throw InvalidUser("Street cannot be empty")
        }
        if (user.address.town.isEmpty()) {
            throw InvalidUser("Town cannot be empty")
        }
        if (user.email.isEmpty()) {
            throw InvalidUser("Email cannot be empty")
        }
        if (user.phone.isEmpty()) {
            throw InvalidUser("Phone cannot be empty")
        }
        if (user.password.isEmpty()) {
            throw InvalidUser("Password cannot be empty")
        }

        // verify email
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!emailPattern.matches(user.email)) {
            throw InvalidUser("Invalid email")
        }

        // verify phone
        val phonePattern = Regex("^((\\+34)|(0034))?([6|7|8|9][0-9]{8})\$")
        if (!phonePattern.matches(user.phone)) {
            throw InvalidUser("Invalid phone number")
        }

        // verify date
        getDateFromUserResponse(user)
    }

    fun getDateFromUserResponse(userRegistryRequest: UserResponse): String {
        // check date
        val dateToSend: String
        try {
            // user must be adult (18 years old minimum)
            val dbDateFormat = SimpleDateFormat(Constants.DB_DATE)
            val appDateFormat = SimpleDateFormat(Constants.APP_DATE)
            val date = appDateFormat.parse(userRegistryRequest.birthDate)
            val minAgeCalendar = Calendar.getInstance()
            minAgeCalendar.add(Calendar.YEAR, -18)
            if (date.after(minAgeCalendar.time)) {
                throw InvalidUser("Must be at least 18 years old")
            }
            val tooOldCalendar = Calendar.getInstance()
            tooOldCalendar.add(Calendar.YEAR, -130)
            if (date.before(tooOldCalendar.time)) {
                throw InvalidUser("Invalid date")
            }
            dateToSend = dbDateFormat.format(date)
        } catch (pe: ParseException) {
            throw InvalidUser("Invalid date, must be in 'dd/MM/yyyy' format")
        }
        return dateToSend
    }
}