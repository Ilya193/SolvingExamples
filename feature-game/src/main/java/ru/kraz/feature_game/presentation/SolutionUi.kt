package ru.kraz.feature_game.presentation

import ru.kraz.common.DelegateItem

data class SolutionUi(
    val id: Int,
    val solution: String,
    val selected: Boolean = false,
) : DelegateItem {
    override fun id(item: DelegateItem): Boolean = id == (item as SolutionUi).id

    override fun compareTo(item: DelegateItem): Boolean = this == (item as SolutionUi)

    override fun changePayload(item: DelegateItem): Any =
        selected != (item as SolutionUi).selected

}