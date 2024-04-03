package ru.kraz.common

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class LevelDb(
    @PrimaryKey
    val id: Int,
    val name: String,
    val passed: Int
)