package feature_users.presentation.registry

import feature_users.domain.model.UserResponse

sealed class RegistryEvent {
    data class FieldEntered(val user: UserResponse): RegistryEvent()
    object Registry: RegistryEvent()
    object Modify: RegistryEvent()
}