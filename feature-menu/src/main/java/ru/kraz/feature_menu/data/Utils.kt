package ru.kraz.feature_menu.data

import ru.kraz.common.LevelDb
import ru.kraz.feature_menu.domain.LevelDomain

object Utils {
    fun LevelDb.toLevelDomain(): LevelDomain = LevelDomain(id, name, passed)
    fun LevelDomain.toLevelDb(): LevelDb = LevelDb(id, name, passed)
}