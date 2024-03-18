package ru.kraz.feature_game.presentation

import ru.kraz.common.Router

interface GameRouter : Router {
    fun openGameResult(solved: Int, unSolved: Int, timeSpent: Int)
}