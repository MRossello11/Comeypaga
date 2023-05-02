package feature_user.data

import core.model.BaseResponse
import feature_user.data.data_source.UserDataSource
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.model.User
import feature_user.domain.repository.UserRepository
import java.text.SimpleDateFormat

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository {
    override suspend fun login(loginRequest: LoginRequest, callback: (User?, errorCode: Int) -> Unit) {
        try{
            val response = userDataSource.login(loginRequest)

            if (!response.isSuccessful){
                callback(null, response.code())
                return
            }

            response.body()?.let {
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

                val user = User(
                    username = it.username,
                    firstname = it.firstname,
                    lastname = it.lastname,
                    birthDate = dateFormatter.parse(it.birthDate),
                    phone = it.phone,
                    email = it.email,
                    address = it.address,
                    password = it.password
                )

                callback(user, response.code())
            } ?: run {
                callback(null, response.code())
            }
        } catch (e: Exception){
            e.printStackTrace()
            callback(null, 400)
        }
    }

    override fun registry() {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest, callback: (response: BaseResponse) -> Unit) {
        val response = userDataSource.resetPassword(resetPasswordRequest)

        callback(BaseResponse(response.code(), response.message()))
    }
}