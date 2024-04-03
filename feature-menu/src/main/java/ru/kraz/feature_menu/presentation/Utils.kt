package ru.kraz.feature_menu.presentation

import ru.kraz.feature_menu.domain.LevelDomain

object Utils {
    fun LevelDomain.toLevelUi(): LevelUi =
        LevelUi(id = id, name = name, levelPassed = passed)
}