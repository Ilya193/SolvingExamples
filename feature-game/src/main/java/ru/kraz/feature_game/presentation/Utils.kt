package ru.kraz.feature_game.presentation

import ru.kraz.feature_game.domain.ExampleDomain

object Utils {
    fun ExampleDomain.toExampleUi(): ExampleUi =
        ExampleUi(id, example, correctAnswer)

    fun ExampleDomain.toSolutionsUi(): SolutionsUi =
        SolutionsUi(id, solutions.toSolutionUi(id))

    private fun List<String>.toSolutionUi(id: Int): MutableList<SolutionUi> = map {
        SolutionUi(id, it)
    }.toMutableList()
}
