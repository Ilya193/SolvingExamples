package ru.kraz.feature_menu.presentation

import ru.kraz.common.Router


interface MenuRouter : Router {
    fun openGame(id: Int, mode: Boolean)
    fun openSettingTimer(id: Int)
}