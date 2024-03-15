package ru.kraz.feature_menu

import ru.kraz.common.Router


interface MenuRouter : Router {
    fun openGame(id: Int, mode: Boolean)
}