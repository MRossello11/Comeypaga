package feature_users.data

import com.google.gson.Gson
import core.Constants.DB_DATE
import core.handleBaseResponse
import core.model.BaseResponse
import core.model.ErrorResponse
import feature_users.data.data_source.UserDataSource
import feature_users.domain.model.*
import feature_users.domain.repository.UserRepository
import java.text.SimpleDateFormat

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
): UserRepository {
    override suspend fun login(loginRequest: LoginRequest, callback: (User?, response: BaseResponse) -> Unit) {
        try{
            val response = userDataSource.login(loginRequest)

            if (response.code() in 200..299) {
                response.body()?.let {
                    val dateFormatter = SimpleDateFormat(DB_DATE)

                    val user = User(
                        _id = it._id,
                        username = it.username,
                        firstname = it.firstname,
                        lastname = it.lastname,
                        birthDate = dateFormatter.parse(it.birthDate),
                        phone = it.phone,
                        email = it.email,
                        address = it.address,
                        password = it.password,
                        role = when(Role.valueOf(it.role)){
                            Role.User -> {
                                Role.User
                            }
                            Role.Rider ->{
                                Role.Rider
                            }
                            Role.Admin ->{
                                Role.Admin
                            }
                        }
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
            e.printStackTrace()
            callback(null, BaseResponse(400, "An error occurred"))
        }
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest, callback: (response: BaseResponse) -> Unit) {
        val response = userDataSource.resetPassword(resetPasswordRequest)

        handleBaseResponse(response, callback)
    }

    override suspend fun registry(userRegistryRequest: UserResponse, callback: (response: BaseResponse) -> Unit) {
        val response = userDataSource.registry(userRegistryRequest)


        handleBaseResponse(response, callback)
    }
}