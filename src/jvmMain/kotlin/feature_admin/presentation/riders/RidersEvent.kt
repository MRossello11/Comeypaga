package feature_admin.presentation.riders

import feature_users.domain.model.User

sealed class RidersEvent {
    data class DeleteRider(val rider: User): RidersEvent()
    object DeletionConfirmed: RidersEvent()
}