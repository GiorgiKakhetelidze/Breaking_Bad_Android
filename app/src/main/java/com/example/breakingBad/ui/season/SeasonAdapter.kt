package com.example.breakingBad.ui.season

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.data.models.character.Episode
import com.example.breakingBad.databinding.SeasonEpisodeItemBinding

class SeasonAdapter(
    private val onItemClick: (character: Episode) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var episodeList: List<Episode> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val onClickListener = View.OnClickListener { v ->
        val episode = v?.tag as Episode
        onItemClick.invoke(episode)
    }

    override fun getItemViewType(position: Int): Int {
        return  VIEW_TYPE_SEASON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_SEASON -> SavedCharacterViewHolder(
                binding = SeasonEpisodeItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            else -> throw RuntimeException("Unknown ViewType")
        }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedCharacterViewHolder -> {
                val item = episodeList[position]
                holder.binding.episodeNumTextView.text = "Episodes" + item.season.toString()
                holder.binding.episodeNameTextView.text = item.title
                holder.binding.root.tag = item
                if (item.title == "Pilot")
                    Glide.with(holder.itemView).load(R.drawable.season_pilot)
                        .into(holder.binding.episodeItemView)
                else
                    Glide.with(holder.itemView).load(R.drawable.season_img_std)
                        .into(holder.binding.episodeItemView)

            }
        }
    }

    override fun getItemCount(): Int = episodeList.size

    //ViewHolder
    class SavedCharacterViewHolder(
        val binding: SeasonEpisodeItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(onClickListener)
        }
    }

    companion object {
        const val VIEW_TYPE_SEASON = 1
    }
}