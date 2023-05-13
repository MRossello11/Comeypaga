package feature_admin.presentation.menu

import core.model.Plate

sealed class MenuEvent{
    data class SetPlates(val plates: List<Plate>): MenuEvent()
    data class DeletePlate(val plate: Plate): MenuEvent()
    object ConfirmDelete: MenuEvent()
    data class SetPlate(val plate: Plate): MenuEvent()
}
