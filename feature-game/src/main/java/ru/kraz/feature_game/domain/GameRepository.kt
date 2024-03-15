package ru.kraz.feature_game.domain

import ru.kraz.common.ResultFDS

interface GameRepository {
    suspend fun fetchLevel(id: Int): ResultFDS<List<ExampleDomain>>
}