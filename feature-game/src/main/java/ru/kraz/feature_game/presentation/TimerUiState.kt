package ru.kraz.feature_game.presentation

sealed interface TimerUiState {
    data object Loading : TimerUiState
    data class Tick(val sec: Int, val time: String) : TimerUiState
    data class Finish(val time: String) : TimerUiState
}