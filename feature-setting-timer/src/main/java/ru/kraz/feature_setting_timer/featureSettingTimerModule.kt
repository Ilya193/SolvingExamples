package ru.kraz.feature_setting_timer

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureSettingTimerModule = module {
    viewModel<SettingTimerViewModel> {
        SettingTimerViewModel(get())
    }
}