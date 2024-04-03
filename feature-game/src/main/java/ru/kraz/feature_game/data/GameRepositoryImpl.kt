package ru.kraz.feature_game.data

import ru.kraz.common.ErrorType
import ru.kraz.common.ResultFDS
import ru.kraz.feature_game.data.cache.ExamplesDao
import ru.kraz.feature_game.data.cache.SolutionDb
import ru.kraz.feature_game.domain.ExampleDomain
import ru.kraz.feature_game.domain.GameRepository
import java.net.UnknownHostException

class GameRepositoryImpl(
    private val dao: ExamplesDao
) : GameRepository {
    override suspend fun fetchLevel(id: Int): ResultFDS<List<ExampleDomain>> {
        return try {
            val examples = dao.fetchExamples(id)
            val solutions = mutableListOf<SolutionDb>()
            examples.forEach { solutions.addAll(dao.fetchSolutions(it.id)) }
            ResultFDS.Success(examples.map { example ->
                val solutionsForExample = solutions.filter { it.exampleId == example.id }
                example.toExampleDomain(solutionsForExample.map { it.solution })
            })
        } catch (e: UnknownHostException) {
            ResultFDS.Error(ErrorType.NO_CONNECTION)
        } catch (e: Exception) {
            ResultFDS.Error(ErrorType.GENERIC_ERROR)
        }
    }

    override suspend fun levelPassed(id: Int) {
        val level = dao.fetchLevel(id)
        dao.levelPassed(level.copy(passed = 1))
    }
}