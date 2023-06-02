package core

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import core.Constants.OrderStates.CANCELED
import core.Constants.OrderStates.CANCELED_TEXT
import core.Constants.OrderStates.CREATED
import core.Constants.OrderStates.CREATED_TEXT
import core.Constants.OrderStates.DELIVERING
import core.Constants.OrderStates.DELIVERING_TEXT
import core.Constants.OrderStates.IN_PROGRESS
import core.Constants.OrderStates.IN_PROGRESS_TEXT
import core.Constants.OrderStates.LATE
import core.Constants.OrderStates.LATE_TEXT
import feature_user.domain.model.Order
import feature_user.domain.model.OrderLine
import feature_user.domain.model.OrderWS
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

    fun mapOrdersWsToOrders(ordersWS: List<OrderWS>): ArrayList<Order> {
        // get string date
        val dbDate = SimpleDateFormat(Constants.DB_DATE)

        val ordersToReturn = arrayListOf<Order>()

        ordersWS.forEach { orderWS ->
            val orderLines = arrayListOf<OrderLine>()
            orderWS.orderLines.forEach { orderLineWS ->
                orderLines.add(
                    OrderLine(
                        plateId = orderLineWS.plateId,
                        plateName = orderLineWS.plateName,
                        quantity = orderLineWS.quantity,
                        price = orderLineWS.price.toFloat()
                    )
                )
            }
            ordersToReturn.add(
                Order(
                    _id = orderWS._id,
                    shippingAddress = orderWS.shippingAddress,
                    state = orderWS.state,
                    arrivalTime = dbDate.parse(orderWS.arrivalTime),
                    restaurantId = orderWS.restaurantId,
                    restaurantName = orderWS.restaurantName,
                    userId = orderWS.userId,
                    orderLines = orderLines
                )
            )
        }
        return ordersToReturn
    }

    fun mapStringStateToCode(stringState: String): Int{
        return when(stringState){
            CREATED_TEXT -> {
                CREATED
            }
            IN_PROGRESS_TEXT -> {
                IN_PROGRESS
            }
            DELIVERING_TEXT -> {
                DELIVERING
            }
            LATE_TEXT -> {
                LATE
            }
            CANCELED_TEXT -> {
                CANCELED
            }
            else -> {
                CANCELED
            }
        }
    }

    fun mapStateCodeToString(state: Int): String{
        return when(state){
            CREATED -> {
                CREATED_TEXT
            }
            IN_PROGRESS -> {
                IN_PROGRESS_TEXT
            }
            DELIVERING -> {
                DELIVERING_TEXT
            }
            LATE -> {
                LATE_TEXT
            }
            CANCELED -> {
                CANCELED_TEXT
            }
            else -> {
                CANCELED_TEXT
            }
        }
    }

    var getOrdersUserMode = false
}