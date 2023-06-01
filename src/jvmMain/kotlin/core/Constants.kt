package core

object Constants {
    object WebService {
        const val BASE_URL = "https://localhost:3000"
        const val USER = "/user"
        const val RESTAURANTS = "/restaurants"
        const val MENU = "/menu"
        const val RIDER = "/rider"
        const val ORDERS = "/orders"
    }

    const val DB_DATE ="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val APP_DATE ="dd/MM/yyyy"

    object PlateTypes{
        const val STARTER = "Starter"
        const val MAIN = "Main"
        const val DESSERT = "Dessert"
        const val DRINK = "Drink"
    }

    object OrderStates {
        const val CREATED = 0
        const val IN_PROGRESS = 1
        const val DELIVERING = 2
        const val LATE = 3
        const val DELIVERED = 4
        const val CANCELED = 5
        const val CREATED_TEXT = "Created"
        const val IN_PROGRESS_TEXT = "In progress"
        const val DELIVERING_TEXT = "Delivering"
        const val LATE_TEXT = "Late"
        const val DELIVERED_TEXT = "Delivered"
        const val CANCELED_TEXT = "Canceled"
    }
}