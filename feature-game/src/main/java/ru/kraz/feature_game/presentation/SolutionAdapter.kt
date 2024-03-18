package ru.kraz.feature_game.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kraz.common.BaseDiffUtil
import ru.kraz.feature_game.R
import ru.kraz.feature_game.databinding.ItemSolutionLayoutBinding

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
                if (item.selected) R.color.blue else R.color.solution
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

class DiffSolution : BaseDiffUtil<SolutionUi>()