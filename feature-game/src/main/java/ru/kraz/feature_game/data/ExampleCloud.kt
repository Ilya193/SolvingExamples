package ru.kraz.feature_game.data

import ru.kraz.feature_game.domain.ExampleDomain

data class ExampleCloud(
    val id: Int,
    val example: String,
    val solutions: List<String>,
    val correctAnswer: Int
) {
    fun toExampleDomain(): ExampleDomain =
        ExampleDomain(id, example, solutions, correctAnswer)
}