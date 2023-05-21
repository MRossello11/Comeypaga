package feature_admin.domain.use_cases

data class AdminUseCases(
    val getRestaurant: GetRestaurant,
    val getRestaurants: GetRestaurants,
    val addRestaurant: AddRestaurant,
    val deleteRestaurant: DeleteRestaurant,
    val addPlate: AddPlate,
    val modifyPlate: ModifyPlate,
    val deletePlate: DeletePlate,
    val getRiders: GetRiders,
    val postRider: PostRider,
    val deleteRider: DeleteRider
)