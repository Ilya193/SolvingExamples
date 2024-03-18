package ru.kraz.feature_game.presentation

import ru.kraz.common.DelegateItem

data class ExampleUi(
    val id: Int,
    val example: String,
    val correctAnswer: Int,
) : DelegateItem {
    override fun id(item: DelegateItem): Boolean = id == (item as ExampleUi).id

    override fun compareTo(item: DelegateItem): Boolean = this == (item as ExampleUi)

    override fun changePayload(item: DelegateItem): Any = false

}