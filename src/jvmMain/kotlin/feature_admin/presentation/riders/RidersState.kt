package feature_admin.presentation.riders

import core.model.BaseResponse
import feature_users.domain.model.User

data class RidersState(
    val actualRider: User? = null,
    val riders: List<User> = listOf(),
    val response: BaseResponse = BaseResponse()
)
