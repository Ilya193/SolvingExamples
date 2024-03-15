package ru.kraz.feature_game.presentation

sealed interface GameUiState {
    data class Success(
        val examples: List<ExampleUi>,
        val solutions: List<SolutionUi>,
        val solvedExamples: List<SolvedExample>,
    ) : GameUiState

    data object Loading : GameUiState
    data class Error(val msg: Int) : GameUiState
}