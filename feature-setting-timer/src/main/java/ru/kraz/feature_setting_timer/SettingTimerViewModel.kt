package ru.kraz.feature_setting_timer

import androidx.lifecycle.ViewModel

class SettingTimerViewModel(
    private val router: SettingTimerRouter
): ViewModel() {

    fun openGame(id: Int, seconds: Int) = router.openGame(id, seconds)
}