package feature_user.domain.use_cases

data class UserUseCases(
    val loginUseCase: LoginUseCase,
    val resetPassword: RestPasswordUseCase
)
