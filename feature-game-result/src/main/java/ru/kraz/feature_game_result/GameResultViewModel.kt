package ru.kraz.feature_game_result

import androidx.lifecycle.ViewModel

class GameResultViewModel(
    private val router: GameResultRouter
) : ViewModel() {

    fun openMenu() = router.openMenu()
}