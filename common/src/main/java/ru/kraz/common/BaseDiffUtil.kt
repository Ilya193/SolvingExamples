package ru.kraz.common

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtil<T: DelegateItem> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.id(newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.compareTo(newItem)

    override fun getChangePayload(oldItem: T, newItem: T): Any? =
        oldItem.changePayload(newItem)

}