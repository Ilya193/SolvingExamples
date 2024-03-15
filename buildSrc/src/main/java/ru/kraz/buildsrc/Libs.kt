package ru.kraz.buildsrc

object Libs {
    const val koin = "io.insert-koin:koin-android:${Versions.koin}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val converterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.converterMoshi}"
}

object Versions {
    const val koin = "3.1.4"
    const val fragmentKtx = "1.6.2"
    const val coroutines = "1.7.3"
    const val retrofit = "2.9.0"
    const val moshi = "1.15.0"
    const val converterMoshi = "2.9.0"
}