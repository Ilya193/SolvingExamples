package ru.kraz.feature_game_result

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureGameResultModule = module {
    viewModel<GameResultViewModel> {
        GameResultViewModel(get())
    }
}