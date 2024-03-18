package ru.kraz.feature_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kraz.common.BaseDiffUtil
import ru.kraz.common.BaseListAdapter
import ru.kraz.feature_menu.databinding.ItemLevelLayoutBinding

class LevelsAdapter(
    private val expand: (Int) -> Unit,
    private val start: (Int, Boolean) -> Unit,
    private val settingTimer: (Int) -> Unit
) : BaseListAdapter<LevelUi, LevelsAdapter.ViewHolder>(DiffLevels()) {
    inner class ViewHolder(private val view: ItemLevelLayoutBinding) : BaseViewHolder<LevelUi>(view.root) {

        init {
            view.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    expand(adapterPosition)
            }

            view.tvStart.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    start(getItem(adapterPosition).id, view.checkClock.isChecked)
            }

            view.checkClock.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) settingTimer(getItem(adapterPosition).id)
            }
        }

        override fun bind(item: LevelUi) {
            view.tvLevel.text = item.name
            bindSettingStart(item)
        }

        fun bindSettingStart(item: LevelUi) {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else holder.bindSettingStart(getItem(position))
    }
}

class DiffLevels : BaseDiffUtil<LevelUi>()