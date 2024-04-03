package ru.kraz.feature_game.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.kraz.common.LevelDb
import ru.kraz.feature_game.domain.ExampleDomain

@Entity(
    tableName = "examples",
    foreignKeys = [ForeignKey(
        entity = LevelDb::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("level_id"),
        onUpdate = ForeignKey.CASCADE
    )]
)
data class ExampleDb(
    @PrimaryKey
    val id: Int,
    val example: String,
    @ColumnInfo("correct_answer")
    val correctAnswer: Int,
    @ColumnInfo("level_id")
    val levelId: Int
) {
    fun toExampleDomain(solutions: List<String>): ExampleDomain =
        ExampleDomain(id, example, solutions, correctAnswer)
}