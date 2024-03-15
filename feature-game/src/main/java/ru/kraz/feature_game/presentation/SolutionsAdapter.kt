package ru.kraz.feature_game.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kraz.feature_game.R
import ru.kraz.feature_game.databinding.ItemSolutionLayoutBinding
import ru.kraz.feature_game.databinding.ItemsSolutionLayoutBinding

class SolutionsAdapter(
    private val select: (Int, Int) -> Unit
) : ListAdapter<SolutionsUi, SolutionsAdapter.ViewHolder>(DiffSolutions()) {

    inner class ViewHolder(private val view: ItemsSolutionLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {

        private val adapter = SolutionAdapter {
            select(adapterPosition, it)
        }

        init {
            view.root.adapter = adapter
        }

        fun bind(item: SolutionsUi) {
            adapter.submitList(item.solutions)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemsSolutionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SolutionAdapter(
    private val select: (Int) -> Unit
) : ListAdapter<SolutionUi, SolutionAdapter.ViewHolder>(DiffSolution()) {

    inner class ViewHolder(private val view: ItemSolutionLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {

        init {
            view.root.setOnClickListener {
                select(adapterPosition)
            }
        }

        fun bind(item: SolutionUi) {
            view.tvSolution.text = item.solution
            bindSelect(item)
        }

        fun bindSelect(item: SolutionUi) {
            val color = ContextCompat.getColor(
                view.root.context,
                if (item.selected) R.color.blue else R.color.dark
            )
            view.root.setCardBackgroundColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolutionAdapter.ViewHolder =
        ViewHolder(
            ItemSolutionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SolutionAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else holder.bindSelect(getItem(position))
    }
}

class DiffSolutions : DiffUtil.ItemCallback<SolutionsUi>() {
    override fun areItemsTheSame(oldItem: SolutionsUi, newItem: SolutionsUi): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SolutionsUi, newItem: SolutionsUi): Boolean =
        oldItem == newItem

}

class DiffSolution : DiffUtil.ItemCallback<SolutionUi>() {
    override fun areItemsTheSame(oldItem: SolutionUi, newItem: SolutionUi): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SolutionUi, newItem: SolutionUi): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: SolutionUi, newItem: SolutionUi): Any? =
        oldItem.selected != newItem.selected

}