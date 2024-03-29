pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SolvingExamples"
include(":app")
include(":feature-menu")
include(":common")
include(":feature-game")
include(":feature-game-result")
include(":feature-setting-timer")
