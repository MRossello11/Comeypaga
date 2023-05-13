package feature_admin.domain.use_cases

data class AdminUseCases(
    val getRestaurants: GetRestaurants,
    val addRestaurant: AddRestaurant,
    val deleteRestaurant: DeleteRestaurant,
    val addPlate: AddPlate,
    val modifyPlate: ModifyPlate,
    val deletePlate: DeletePlate
)