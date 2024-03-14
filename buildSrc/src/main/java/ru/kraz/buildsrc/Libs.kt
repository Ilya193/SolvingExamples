package ru.kraz.buildsrc

object Libs {
    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}

object Versions {
    const val koin = "3.1.4"
    const val fragmentKtx = "1.6.2"
    const val coroutines = "1.7.3"
}