package ru.kraz.feature_menu.data

import ru.kraz.feature_menu.data.Utils.toLevelDomain
import ru.kraz.feature_menu.domain.LevelDomain
import ru.kraz.feature_menu.domain.MenuRepository

class MenuRepositoryImpl(
    private val dao: LevelsDao
) : MenuRepository {
    override suspend fun fetchLevels(): List<LevelDomain> = dao.fetchLevels().map { it.toLevelDomain() }
}