package feature_admin.presentation.add_modify_plate

import core.model.Plate
import core.model.Restaurant

sealed class AddModifyPlateEvent{
    data class FieldEntered(val plate: Plate): AddModifyPlateEvent()
    data class CreatePlate(val restaurant: Restaurant): AddModifyPlateEvent()
    data class ModifyPlate(val restaurant: Restaurant): AddModifyPlateEvent()
}
