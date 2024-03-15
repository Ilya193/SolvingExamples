package ru.kraz.feature_game.domain

data class ExampleDomain(
    val id: Int,
    val example: String,
    val solutions: List<String>,
    val correctAnswer: Int
)