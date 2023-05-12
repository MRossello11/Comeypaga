package feature_admin.presentation.menu

import core.model.Plate

data class MenuState(
    val menu: List<Plate> = listOf(),
    val actualPlate: Plate? = null
)
