package feature_user.data

import feature_user.data.data_source.UserDataSource
import feature_user.domain.model.LoginRequest
import feature_user.domain.model.User
import feature_user.domain.repository.UserRepository

class UserRepositoryImpl(
    val userDataSource: UserDataSource
): UserRepository {
    override suspend fun login(loginRequest: LoginRequest, callback: (User?, errorCode: Int) -> Unit) {
        try{
            val response = userDataSource.login(loginRequest)

            if (!response.isSuccessful){
                callback(null, response.code())
                return
            }

            callback(response.body(), response.code())
        } catch (e: Exception){
            callback(null, 400)
        }
    }

    override fun registry() {
        TODO("Not yet implemented")
    }
}