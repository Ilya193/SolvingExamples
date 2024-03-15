package ru.kraz.common

interface StringErrorProvider {
    fun getData(e: ErrorType): Int
}