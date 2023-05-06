package feature_users.presentation.registry

sealed class RegistryEvent {
    data class FieldEntered(val value: String, val fieldEntered: Field): RegistryEvent()
    object Registry: RegistryEvent()
}