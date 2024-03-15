package ru.kraz.feature_game.data

import ru.kraz.common.ErrorType
import ru.kraz.common.ResultFDS
import ru.kraz.feature_game.domain.ExampleDomain
import ru.kraz.feature_game.domain.GameRepository
import java.net.UnknownHostException

class GameRepositoryImpl(
    private val service: GameService
) : GameRepository {
    override suspend fun fetchLevel(id: Int): ResultFDS<List<ExampleDomain>> {
        return try {
            val data = service.fetchLevel(id)
            ResultFDS.Success(data.map { it.toExampleDomain() })
        } catch (e: UnknownHostException) {
            ResultFDS.Error(ErrorType.NO_CONNECTION)
        } catch (e: Exception) {
            ResultFDS.Error(ErrorType.GENERIC_ERROR)
        }
    }
}