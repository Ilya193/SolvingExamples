package ru.kraz.feature_game.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.kraz.feature_game.data.GameRepositoryImpl
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.GameViewModel

val featureGameModule = module {
    factory<GameRepository> {
        GameRepositoryImpl(get())
    }

    viewModel<GameViewModel> { params ->
        GameViewModel(router = get(), levelId = params.get(), repository = get())
    }
}