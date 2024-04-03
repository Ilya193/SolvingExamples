package ru.kraz.buildsrc

object Libs {
    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
}

object Versions {
    const val koin = "3.1.4"
    const val fragmentKtx = "1.6.2"
    const val coroutines = "1.7.3"
    const val room = "2.6.1"
}