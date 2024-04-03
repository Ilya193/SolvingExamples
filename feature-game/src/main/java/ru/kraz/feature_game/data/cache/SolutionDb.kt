package ru.kraz.feature_game.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "solutions",
    foreignKeys = [ForeignKey(
        entity = ExampleDb::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("example_id"),
        onUpdate = ForeignKey.CASCADE
    )]
)
data class SolutionDb(
    @PrimaryKey
    val id: Int,
    val solution: String,
    @ColumnInfo("example_id")
    val exampleId: Int
)