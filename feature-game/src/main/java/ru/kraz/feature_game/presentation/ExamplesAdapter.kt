package ru.kraz.feature_game.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kraz.feature_game.databinding.ItemExampleLayoutBinding

class ExamplesAdapter : ListAdapter<ExampleUi, ExamplesAdapter.ViewHolder>(DiffExamples()) {

    inner class ViewHolder(private val view: ItemExampleLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: ExampleUi) {
            view.tvExample.text = item.example
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemExampleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffExamples : DiffUtil.ItemCallback<ExampleUi>() {
    override fun areItemsTheSame(oldItem: ExampleUi, newItem: ExampleUi): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ExampleUi, newItem: ExampleUi): Boolean =
        oldItem == newItem

}