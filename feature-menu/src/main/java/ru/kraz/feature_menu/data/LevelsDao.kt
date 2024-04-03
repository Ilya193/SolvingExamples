package ru.kraz.feature_menu.data

import androidx.room.Dao
import androidx.room.Query
import ru.kraz.common.LevelDb

@Dao
interface LevelsDao {

    @Query("SELECT * from levels")
    suspend fun fetchLevels(): List<LevelDb>
}