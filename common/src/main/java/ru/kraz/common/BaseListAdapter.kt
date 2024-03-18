package ru.kraz.common

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<T : DelegateItem, R : BaseListAdapter.BaseViewHolder<T>>(
    diffUtil: BaseDiffUtil<T>,
) : ListAdapter<T, R>(diffUtil) {

    abstract class BaseViewHolder<T : DelegateItem>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T)
    }

    override fun onBindViewHolder(holder: R, position: Int) {
        holder.bind(getItem(position))
    }
}