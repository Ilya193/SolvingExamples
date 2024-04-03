package ru.kraz.feature_menu.domain

interface MenuRepository {
    suspend fun fetchLevels(): List<LevelDomain>
}