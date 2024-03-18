package ru.kraz.solvingexamples

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kraz.feature_game.presentation.GameFragment
import ru.kraz.feature_game.presentation.GameRouter
import ru.kraz.feature_game_result.GameResultFragment
import ru.kraz.feature_game_result.GameResultRouter
import ru.kraz.feature_menu.MenuFragment
import ru.kraz.feature_menu.MenuRouter
import ru.kraz.feature_setting_timer.SettingTimerFragment
import ru.kraz.feature_setting_timer.SettingTimerRouter

interface Navigation<T> {
    fun read(): StateFlow<T>
    fun update(value: T)

    class Base : Navigation<Screen>, MenuRouter, GameRouter, GameResultRouter, SettingTimerRouter {
        private val screen = MutableStateFlow<Screen>(Screen.Empty)

        override fun read(): StateFlow<Screen> = screen

        override fun update(value: Screen) {
            screen.value = value
        }

        override fun openGame(id: Int, mode: Boolean) {
            update(GameScreen(id, mode))
        }

        override fun openGame(id: Int, maxSec: Int) {
            update(GameScreen(id, true, maxSec))
        }

        override fun openSettingTimer(id: Int) {
            update(SettingTimerScreen(id))
        }

        override fun openGameResult(
            levelId: Int,
            solved: Int,
            unSolved: Int,
            timeSpent: Int,
            levelPassed: Boolean
        ) {
            update(GameResultScreen(levelId, solved, unSolved, timeSpent, levelPassed))
        }

        override fun openMenu(id: Int) {
            update(MenuScreen(id))
        }

        override fun coup() {
            update(Screen.Coup)
        }

        override fun comeback() {
            update(Screen.Pop)
        }
    }
}

interface Screen {
    fun show(supportFragmentManager: FragmentManager, container: Int) = Unit

    abstract class Replace(
        private val fragment: Fragment
    ) : Screen {
        override fun show(supportFragmentManager: FragmentManager, container: Int) {
            supportFragmentManager.commit {
                replace(container, fragment)
            }
        }
    }

    abstract class ReplaceWithClear(
        fragment: Fragment
    ) : Replace(fragment) {
        override fun show(supportFragmentManager: FragmentManager, container: Int) {
            repeat(supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
            super.show(supportFragmentManager, container)
        }
    }

    abstract class ReplaceWithAddToBackStack(
        private val fragment: Fragment,
        private val name: String? = null
    ) : Screen {
        override fun show(supportFragmentManager: FragmentManager, container: Int) {
            supportFragmentManager.commit {
                replace(container, fragment)
                addToBackStack(name)
            }
        }
    }

    abstract class BottomSheet(
        private val fragment: BottomSheetDialogFragment
    ) : Screen {
        override fun show(supportFragmentManager: FragmentManager, container: Int) {
            fragment.show(supportFragmentManager, null)
        }
    }

    data object Pop : Screen {
        override fun show(supportFragmentManager: FragmentManager, container: Int) {
            supportFragmentManager.popBackStack()
        }
    }

    data object Coup : Screen

    data object Empty : Screen
}

class MenuScreen(
    id: Int = -1
) : Screen.Replace(MenuFragment.newInstance(id))
class GameScreen(
    id: Int,
    mode: Boolean,
    seconds: Int = 3600
) : Screen.ReplaceWithAddToBackStack(fragment = GameFragment.newInstance(id, mode, seconds))

class GameResultScreen(
    levelId: Int,
    solved: Int,
    unSolved: Int,
    timeSpent: Int,
    levelPassed: Boolean
) : Screen.ReplaceWithClear(
    fragment = GameResultFragment.newInstance(
        levelId,
        solved,
        unSolved,
        timeSpent,
        levelPassed
    )
)

class SettingTimerScreen(
    id: Int,
) : Screen.BottomSheet(SettingTimerFragment.newInstance(id))