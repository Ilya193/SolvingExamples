package ru.kraz.feature_menu.presentation

import ru.kraz.common.DelegateItem

data class LevelUi(
    val id: Int,
    val name: String,
    val expanded: Boolean = false,
    val levelPassed: Int = 0
): DelegateItem {

    override fun id(item: DelegateItem): Boolean = id == (item as LevelUi).id

    override fun compareTo(item: DelegateItem): Boolean = this == (item as LevelUi)

    override fun changePayload(item: DelegateItem): Any = false

}