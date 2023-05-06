package feature_users.domain.use_cases

import core.Constants.APP_DATE
import core.Constants.DB_DATE
import core.model.BaseResponse
import feature_users.domain.model.InvalidUser
import feature_users.domain.model.UserResponse
import feature_users.domain.repository.UserRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RegistryUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userRegistryRequest: UserResponse,
        callback: (response: BaseResponse) -> Unit
    ) {
        // verify fields
        if (userRegistryRequest.username.isEmpty()){
            throw InvalidUser("Username cannot be empty")
        }
        if (userRegistryRequest.firstname.isEmpty()){
            throw InvalidUser("Firstname cannot be empty")
        }
        if (userRegistryRequest.lastname.isEmpty()){
            throw InvalidUser("Lastname cannot be empty")
        }
        if (userRegistryRequest.birthDate.isEmpty()){
            throw InvalidUser("Birth date cannot be empty")
        }
        if (userRegistryRequest.address.street.isEmpty()){
            throw InvalidUser("Street cannot be empty")
        }
        if (userRegistryRequest.address.town.isEmpty()){
            throw InvalidUser("Town cannot be empty")
        }
        if (userRegistryRequest.email.isEmpty()){
            throw InvalidUser("Email cannot be empty")
        }
        if (userRegistryRequest.phone.isEmpty()){
            throw InvalidUser("Phone cannot be empty")
        }
        if (userRegistryRequest.password.isEmpty()){
            throw InvalidUser("Password cannot be empty")
        }

        // check date
        val dateToSend: String
        try{
            // user must be adult (18 years old minimum)
            val dbDateFormat = SimpleDateFormat(DB_DATE)
            val appDateFormat = SimpleDateFormat(APP_DATE)
            val date = appDateFormat.parse(userRegistryRequest.birthDate)
            val minAgeCalendar = Calendar.getInstance()
            minAgeCalendar.add(Calendar.YEAR, -18)
            if (date.after(minAgeCalendar.time)) {
                throw InvalidUser("You must be at least 18 years old")
            }
            val tooOldCalendar = Calendar.getInstance()
            tooOldCalendar.add(Calendar.YEAR, -130)
            if (date.before(tooOldCalendar.time)){
                throw InvalidUser("Invalid date")
            }
            dateToSend = dbDateFormat.format(date)
        } catch (pe: ParseException){
            throw InvalidUser("Invalid date, must be in 'dd/MM/yyyy' format")
        }

        // verify email
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!emailPattern.matches(userRegistryRequest.email)) {
            throw InvalidUser("Invalid email")
        }

        // verify phone
        val phonePattern = Regex("^((\\+34)|(0034))?([6|7|8|9][0-9]{8})\$")
        if (!phonePattern.matches(userRegistryRequest.phone)) {
            throw InvalidUser("Invalid phone number")
        }

        userRepository.registry(
            userRegistryRequest.copy(
                birthDate = dateToSend
            ),
            callback
        )
    }

}