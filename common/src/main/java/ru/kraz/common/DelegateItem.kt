package ru.kraz.common

interface DelegateItem {
    fun id(item: DelegateItem): Boolean
    fun compareTo(item: DelegateItem): Boolean
    fun changePayload(item: DelegateItem): Any
}