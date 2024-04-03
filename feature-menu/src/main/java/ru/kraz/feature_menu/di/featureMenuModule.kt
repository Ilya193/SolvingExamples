package ru.kraz.feature_menu.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.kraz.feature_menu.data.MenuRepositoryImpl
import ru.kraz.feature_menu.domain.MenuRepository
import ru.kraz.feature_menu.presentation.MenuViewModel

val featureMenuModule = module {
    factory<MenuRepository> {
        MenuRepositoryImpl(get())
    }

    viewModel<MenuViewModel> {
        MenuViewModel(get(), get())
    }
}