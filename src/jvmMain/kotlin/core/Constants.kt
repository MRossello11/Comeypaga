package core

object Constants {
    object WebService {
        const val BASE_URL = "http://localhost:3000"
        const val USER = "/user"
        const val RESTAURANTS = "/restaurants"
        const val MENU = "/menu"
    }

    const val DB_DATE ="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val APP_DATE ="dd/MM/yyyy"

    object PlateTypes{
        const val STARTER = "Starter"
        const val MAIN = "Main"
        const val DESSERT = "Dessert"
        const val DRINK = "Drink"
    }
}