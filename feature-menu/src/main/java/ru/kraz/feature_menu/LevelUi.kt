package ru.kraz.feature_menu

data class LevelUi(
    val id: Int,
    val name: String,
    val expanded: Boolean = false,
    val clock: Boolean = false
)