package ru.kraz.solvingexamples

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kraz.common.LevelDb
import ru.kraz.feature_game.data.cache.ExampleDb
import ru.kraz.feature_game.data.cache.ExamplesDao
import ru.kraz.feature_game.data.cache.SolutionDb
import ru.kraz.feature_menu.data.LevelsDao

@Database(entities = [LevelDb::class, ExampleDb::class, SolutionDb::class], version = 1)
abstract class LevelsDb : RoomDatabase() {
    abstract fun examplesDao(): ExamplesDao
    abstract fun levelsDao(): LevelsDao
}