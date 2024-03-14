package ru.kraz.solvingexamples

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
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

    viewModel<MainViewModel> {
        MainViewModel(get())
    }

    viewModel<MenuViewModel> {
        MenuViewModel(get())
    }
}