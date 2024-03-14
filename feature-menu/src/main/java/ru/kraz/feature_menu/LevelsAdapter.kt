package ru.kraz.feature_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kraz.feature_menu.databinding.ItemLevelLayoutBinding

class LevelsAdapter(
    private val expand: (Int) -> Unit,
    private val start: (Int, Boolean) -> Unit
) : ListAdapter<LevelUi, LevelsAdapter.ViewHolder>(Diff()) {

    inner class ViewHolder(private val view: ItemLevelLayoutBinding) : RecyclerView.ViewHolder(view.root) {

        init {
            view.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    expand(adapterPosition)
            }

            view.tvStart.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    start(adapterPosition, view.checkClock.isChecked)
            }
        }

        fun bind(item: LevelUi) {
            view.tvLevel.text = item.name
            bindInfo(item)
        }

        fun bindInfo(item: LevelUi) {
            if (item.expanded) {
                view.boxInfo.visibility = View.VISIBLE
                view.boxInfo.animate().translationY(0f).alpha(1f).setDuration(350).start()
            }
            else {
                view.boxInfo.animate().translationY(-80f).alpha(0f).setDuration(350).withEndAction {
                    view.boxInfo.visibility = View.GONE
                }.start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemLevelLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else holder.bindInfo(getItem(position))
    }
}

class Diff : DiffUtil.ItemCallback<LevelUi>() {

    override fun areItemsTheSame(oldItem: LevelUi, newItem: LevelUi): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: LevelUi, newItem: LevelUi): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: LevelUi, newItem: LevelUi): Any? =
        oldItem.expanded != newItem.expanded
}