package com.example.breakingBad.ui.episodeDetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.EpisodeDetailItemBinding
import com.example.breakingBad.databinding.LoadingItemBinding

class EpisodeAdapter(
    private val onItemClick: (character: Character) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList: List<Character> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val onClickListener = View.OnClickListener { v ->
        val character = v?.tag as Character
        onItemClick.invoke(character)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_EPISODE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_EPISODE -> EpisodeCharacterViewHolder(
                binding = EpisodeDetailItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            else -> throw RuntimeException("Unknown ViewType")
        }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EpisodeCharacterViewHolder -> {
                val item = characterList[position]
                holder.binding.savedCharacterNameTextView.text = item.name
                holder.binding.savedCharacterNickNameTextView.text = item.nickname
                holder.binding.savedCharacterPortrayedTextView.text = "by\n" + item.portrayed
                holder.binding.root.tag = item
                Glide.with(holder.itemView).load(item.img)
                    .into(holder.binding.savedCharItemView)
            }
        }
    }

    override fun getItemCount(): Int = characterList.size

    //ViewHolders
    class EpisodeCharacterViewHolder(
        val binding: EpisodeDetailItemBinding,
        onClickListener: View.OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(onClickListener)
        }
    }

    companion object {
        const val VIEW_TYPE_EPISODE = 1
    }
}