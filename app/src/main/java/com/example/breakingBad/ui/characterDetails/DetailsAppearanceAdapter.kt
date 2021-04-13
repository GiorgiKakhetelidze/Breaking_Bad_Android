package com.example.breakingBad.ui.characterDetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingBad.R
import com.example.breakingBad.databinding.AppearenceItemBinding
import com.example.breakingBad.data.models.character.Character


class DetailsAppearanceAdapter(
    private val onItemClick: (str: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList: List<Character> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val onClickListener = View.OnClickListener { v ->
        val str = v?.tag as String
        onItemClick.invoke(str)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_APPEARANCE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_APPEARANCE -> SeasonAppearanceHolder(
                binding = AppearenceItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            else -> throw RuntimeException("Unknown ViewType")
        }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SeasonAppearanceHolder -> {
                val item = characterList.first()
                val betterCallSaulAppearance = item.betterCallSaulAppearance
                val breakingBadAppearance = item.appearance
                val bothSeries = betterCallSaulAppearance + breakingBadAppearance

                if (betterCallSaulAppearance.isNotEmpty() && position < betterCallSaulAppearance.size) {
                    holder.binding.seasonTxtView.background = getDrawable(
                        holder.itemView.resources,
                        R.drawable.season_bcs_logo,
                        null
                    )
                    val isBcsLogo = true
                    val seasonNum = bothSeries[position]
                    holder.binding.seasonTxtView.text =
                        "\nSEASON\n$seasonNum"
                    holder.binding.root.tag = "$isBcsLogo|$seasonNum"
                } else if (breakingBadAppearance.isNotEmpty()) {
                    holder.binding.seasonTxtView.background = getDrawable(
                        holder.itemView.resources,
                        R.drawable.season_bb_logo,
                        null
                    )
                    val isBcsLogo = false
                    val seasonNum = bothSeries[position]
                    holder.binding.seasonTxtView.text =
                        "\nSEASON\n$seasonNum"
                    holder.binding.root.tag = "$isBcsLogo|$seasonNum"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (characterList.isNotEmpty()) {
            val item = characterList.first()
            if (item.appearance.isNotEmpty() && item.betterCallSaulAppearance.isEmpty())
                return item.appearance.size
            if (item.betterCallSaulAppearance.isNotEmpty() && item.appearance.isEmpty())
                return item.betterCallSaulAppearance.size
            if (item.betterCallSaulAppearance.isNotEmpty() && item.appearance.isNotEmpty())
                return (item.betterCallSaulAppearance + item.appearance).size
        }
        return characterList.size
    }

    //ViewHolders
    class SeasonAppearanceHolder(
        val binding: AppearenceItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(onClickListener)
        }
    }

    companion object {
        const val VIEW_TYPE_APPEARANCE = 1
    }
}