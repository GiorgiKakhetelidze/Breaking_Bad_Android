package com.example.breakingBad.ui.savedCharacters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.SavedCharacterItemBinding

class SavedAdapter(
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
        return VIEW_TYPE_SAVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_SAVED -> SavedCharacterViewHolder(
                binding = SavedCharacterItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            else -> throw RuntimeException("Unknown ViewType")
        }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SavedCharacterViewHolder -> {
                val item = characterList[position]
                holder.binding.savedCharacterNameTextView.text = item.name
                holder.binding.savedCharacterNickNameTextView.text = item.nickname
                holder.binding.savedCharacterPortrayedTextView.text = "by\n" + item.portrayed
                holder.binding.savedCharItemView.tag = item
                Glide.with(holder.itemView).load(item.img)
                    .into(holder.binding.savedCharItemView)
            }
        }
    }

    override fun getItemCount(): Int = characterList.size

    //ViewHolder
    class SavedCharacterViewHolder(
        val binding: SavedCharacterItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.savedCharItemView.setOnClickListener(onClickListener)
        }
    }

    companion object {
        const val VIEW_TYPE_SAVED = 1
    }
}