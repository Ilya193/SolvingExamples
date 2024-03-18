package ru.kraz.solvingexamples

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kraz.feature_game.presentation.GameFragment
import ru.kraz.feature_game.presentation.GameRouter
import ru.kraz.feature_game_result.GameResultFragment
import ru.kraz.feature_game_result.GameResultRouter
import ru.kraz.feature_menu.MenuFragment
import ru.kraz.feature_menu.MenuRouter

interface Navigation<T> {
    fun read(): StateFlow<T>
    fun update(value: T)

    class Base : Navigation<Screen>, MenuRouter, GameRouter, GameResultRouter {
        private val screen = MutableStateFlow<Screen>(Screen.Empty)

        override fun read(): StateFlow<Screen> = screen

        override fun update(value: Screen) {
            screen.value = value
        }

        override fun openGame(id: Int, mode: Boolean) {
            update(GameScreen(id, mode))
        }

        override fun openGameResult(solved: Int, unSolved: Int, timeSpent: Int) {
            update(GameResultScreen(solved, unSolved, timeSpent))
        }

        override fun openMenu() {
            update(MenuScreen())
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

    data object Pop : Screen {
        override fun show(supportFragmentManager: FragmentManager, container: Int) {
            supportFragmentManager.popBackStack()
        }
    }

    data object Coup : Screen

    data object Empty : Screen
}

class MenuScreen : Screen.Replace(MenuFragment.newInstance())
class GameScreen(
    id: Int,
    mode: Boolean
) : Screen.ReplaceWithAddToBackStack(fragment = GameFragment.newInstance(id, mode))

class GameResultScreen(
    solved: Int,
    unSolved: Int,
    timeSpent: Int
) : Screen.ReplaceWithClear(fragment = GameResultFragment.newInstance(solved, unSolved, timeSpent))