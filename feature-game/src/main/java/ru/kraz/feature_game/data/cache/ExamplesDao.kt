package ru.kraz.feature_game.data.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import ru.kraz.common.LevelDb

@Dao
interface ExamplesDao {

    @Query("SELECT * FROM levels WHERE id = :levelId")
    suspend fun fetchLevel(levelId: Int): LevelDb

    @Update
    suspend fun levelPassed(level: LevelDb)

    @Query("SELECT * FROM examples WHERE level_id = :levelId")
    suspend fun fetchExamples(levelId: Int): List<ExampleDb>

    @Query("SELECT * FROM solutions WHERE example_id = :id")
    suspend fun fetchSolutions(id: Int): List<SolutionDb>
}