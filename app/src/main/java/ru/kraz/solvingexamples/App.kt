package ru.kraz.solvingexamples

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.kraz.common.StringErrorProvider
import ru.kraz.feature_game.data.GameRepositoryImpl
import ru.kraz.feature_game.data.cache.ExamplesDao
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.BaseStringErrorProvider
import ru.kraz.feature_game.presentation.GameRouter
import ru.kraz.feature_game.presentation.GameViewModel
import ru.kraz.feature_game_result.GameResultRouter
import ru.kraz.feature_game_result.GameResultViewModel
import ru.kraz.feature_menu.data.LevelsDao
import ru.kraz.feature_menu.data.MenuRepositoryImpl
import ru.kraz.feature_menu.domain.MenuRepository
import ru.kraz.feature_menu.presentation.MenuRouter
import ru.kraz.feature_menu.presentation.MenuViewModel
import ru.kraz.feature_setting_timer.SettingTimerRouter
import ru.kraz.feature_setting_timer.SettingTimerViewModel

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}

val appModule = module {
    val navigation = Navigation.Base()

    single<Navigation<Screen>> {
        navigation
    }

    single<MenuRouter> {
        navigation
    }

    single<GameRouter> {
        navigation
    }

    single<GameResultRouter> {
        navigation
    }

    single<SettingTimerRouter> {
        navigation
    }

    factory<GameRepository> {
        GameRepositoryImpl(get())
    }

    factory<StringErrorProvider> {
        BaseStringErrorProvider()
    }

    factory<MenuRepository> {
        MenuRepositoryImpl(get())
    }

    single<LevelsDb> {
        Room.databaseBuilder(get(), LevelsDb::class.java, "examples_db")
            .createFromAsset("examples.db")
            .build()
    }

    factory<LevelsDao> {
        get<LevelsDb>().levelsDao()
    }

    factory<ExamplesDao> {
        get<LevelsDb>().examplesDao()
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }

    viewModel<MenuViewModel> {
        MenuViewModel(get(), get())
    }

    viewModel<GameViewModel> { params ->
        GameViewModel(router = get(), levelId = params.get(), repository = get())
    }

    viewModel<GameResultViewModel> {
        GameResultViewModel(get())
    }

    viewModel<SettingTimerViewModel> {
        SettingTimerViewModel(get())
    }
}