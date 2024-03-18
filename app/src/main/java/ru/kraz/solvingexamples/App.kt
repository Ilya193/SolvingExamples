package ru.kraz.solvingexamples

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.kraz.common.StringErrorProvider
import ru.kraz.feature_game.data.GameRepositoryImpl
import ru.kraz.feature_game.data.GameService
import ru.kraz.feature_game.domain.GameRepository
import ru.kraz.feature_game.presentation.BaseStringErrorProvider
import ru.kraz.feature_game.presentation.GameRouter
import ru.kraz.feature_game.presentation.GameViewModel
import ru.kraz.feature_game_result.GameResultRouter
import ru.kraz.feature_game_result.GameResultViewModel
import ru.kraz.feature_menu.MenuRouter
import ru.kraz.feature_menu.MenuViewModel
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

    single<GameService> {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8080/")
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            .build()
            .create(GameService::class.java)
    }

    factory<GameRepository> {
        GameRepositoryImpl(get())
    }

    factory<StringErrorProvider> {
        BaseStringErrorProvider()
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }

    viewModel<MenuViewModel> {
        MenuViewModel(get())
    }

    viewModel<GameViewModel> {
        GameViewModel(get(), get())
    }

    viewModel<GameResultViewModel> {
        GameResultViewModel(get())
    }

    viewModel<SettingTimerViewModel> {
        SettingTimerViewModel(get())
    }
}