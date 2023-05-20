package feature_admin.domain.use_cases

import core.Constants
import core.model.BaseResponse
import feature_admin.domain.repository.AdminRepository
import feature_users.domain.model.Role
import feature_users.domain.model.User
import java.text.SimpleDateFormat

class GetRiders(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        callback: (response: BaseResponse, riders: List<User>) -> Unit
    ) {
        var baseResponse = BaseResponse()
        val riderList = arrayListOf<User>()

        // get data
        adminRepository.getRiders { response, riders ->
            // set response
            baseResponse = response

            val dateFormatter = SimpleDateFormat(Constants.DB_DATE)
            // map users
            riders.forEach {
                riderList.add(
                    User(
                        _id = it._id,
                        username = it.username,
                        firstname = it.firstname,
                        lastname = it.lastname,
                        birthDate = dateFormatter.parse(it.birthDate),
                        phone = it.phone,
                        email = it.email,
                        address = it.address,
                        password = it.password,
                        role = Role.Rider
                    )
                )
            }

        }

        callback(baseResponse, riderList)
    }
}