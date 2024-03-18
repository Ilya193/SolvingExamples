package ru.kraz.feature_game.presentation

import ru.kraz.common.DelegateItem

data class SolvedExample(
    val id: Int,
    val level: String,
    val solved: Boolean? = null,
) : DelegateItem {
    override fun id(item: DelegateItem): Boolean = id == (item as SolvedExample).id

    override fun compareTo(item: DelegateItem): Boolean = this == (item as SolvedExample)

    override fun changePayload(item: DelegateItem): Any =
        solved != (item as SolvedExample).solved

}
