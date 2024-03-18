package ru.kraz.feature_game.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kraz.common.BaseDiffUtil
import ru.kraz.common.BaseListAdapter
import ru.kraz.feature_game.R
import ru.kraz.feature_game.databinding.ItemSolvedLayoutBinding

class SolvedExamplesAdapter : BaseListAdapter<SolvedExample, SolvedExamplesAdapter.ViewHolder>(DiffSolved()) {
    inner class ViewHolder(private val view: ItemSolvedLayoutBinding) : BaseViewHolder<SolvedExample>(view.root) {
        override fun bind(item: SolvedExample) {
            view.tvSolved.text = item.level
            bindSolved(item)
        }

        fun bindSolved(item: SolvedExample) {
            val solved = item.solved
            if (solved != null) {
                val color = ContextCompat.getColor(
                    view.root.context,
                    if (item.solved) R.color.green else R.color.red
                )
                view.root.setCardBackgroundColor(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSolvedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else holder.bindSolved(getItem(position))
    }
}

class DiffSolved : BaseDiffUtil<SolvedExample>()