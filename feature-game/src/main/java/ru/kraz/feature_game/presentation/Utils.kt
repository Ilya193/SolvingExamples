package ru.kraz.feature_game.presentation

import ru.kraz.feature_game.domain.ExampleDomain

object Utils {
    fun ExampleDomain.toExampleUi(): ExampleUi =
        ExampleUi(id, example, correctAnswer)

    fun ExampleDomain.toSolutionUi(solution: String): SolutionUi =
        SolutionUi(id, solution)
}
