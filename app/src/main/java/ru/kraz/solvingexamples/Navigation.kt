package ru.kraz.solvingexamples

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kraz.feature_game.presentation.GameFragment
import ru.kraz.feature_game.presentation.GameRouter
import ru.kraz.feature_menu.MenuFragment
import ru.kraz.feature_menu.MenuRouter

interface Navigation<T> {
    fun read(): LiveData<T>
    fun update(value: T)

    class Base : Navigation<Screen>, MenuRouter, GameRouter {
        private val screen = MutableLiveData<Screen>()

        override fun read(): LiveData<Screen> = screen

        override fun update(value: Screen) {
            screen.value = value
        }

        override fun openGame(id: Int, mode: Boolean) {
            update(GameScreen(id, mode))
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
}

class MenuScreen : Screen.Replace(MenuFragment.newInstance())
class GameScreen(
    private val id: Int,
    private val mode: Boolean
) : Screen.ReplaceWithAddToBackStack(fragment = GameFragment.newInstance(id, mode))