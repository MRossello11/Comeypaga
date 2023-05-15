package feature_admin.presentation.add_modify_plate

import core.model.Plate

sealed class AddModifyPlateEvent{
    data class FieldEntered(val plate: Plate): AddModifyPlateEvent()
    object CreatePlate: AddModifyPlateEvent()
    object ModifyPlate: AddModifyPlateEvent()
}
