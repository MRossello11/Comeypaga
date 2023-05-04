package feature_user.data

import com.google.gson.Gson
import core.model.BaseResponse
import core.model.ErrorResponse
import feature_user.data.data_source.UserDataSource
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.ResetPasswordRequest
import feature_user.domain.model.User
import feature_user.domain.repository.UserRepository
import java.text.SimpleDateFormat

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository {
    override suspend fun login(loginRequest: LoginRequest, callback: (User?, response: BaseResponse) -> Unit) {
        try{
            val response = userDataSource.login(loginRequest)

            if (response.code() in 200..299) {
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

                    callback(user, BaseResponse(response.code(), response.message()))
                    return
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                callback(null, BaseResponse(response.code(), errorResponse.message ?: "An error occurred"))
                return
            }

            callback(null, BaseResponse(response.code(), "An error occurred"))
        } catch (e: Exception){
            callback(null, BaseResponse(400, "An error occurred"))
        }
    }

    override fun registry() {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest, callback: (response: BaseResponse) -> Unit) {
        val response = userDataSource.resetPassword(resetPasswordRequest)

        if (response.code() in 200..299) {
            response.body()?.let { body ->
                callback(BaseResponse(response.code(), body.message))
                return
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            callback(BaseResponse(response.code(), errorResponse.message ?: "An error occurred"))
            return
        }

        callback(BaseResponse(response.code(), "An error occurred"))
    }
}