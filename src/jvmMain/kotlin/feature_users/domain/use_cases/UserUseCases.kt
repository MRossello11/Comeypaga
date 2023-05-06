package feature_users.domain.use_cases

data class UserUseCases(
    val loginUseCase: LoginUseCase,
    val resetPassword: ResetPasswordUseCase,
    val registryUseCase: RegistryUseCase
)
