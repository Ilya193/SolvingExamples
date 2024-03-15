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
import ru.kraz.feature_game.presentation.GameViewModel
import ru.kraz.feature_menu.MenuRouter
import ru.kraz.feature_menu.MenuViewModel

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

    single<GameService> {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.2:8080/")
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
}