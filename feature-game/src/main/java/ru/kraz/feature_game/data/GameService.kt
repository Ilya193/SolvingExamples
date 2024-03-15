package ru.kraz.feature_game.data

import retrofit2.http.GET
import retrofit2.http.Query

interface GameService {
    @GET("examples")
    suspend fun fetchLevel(@Query("id") id: Int): List<ExampleCloud>
}