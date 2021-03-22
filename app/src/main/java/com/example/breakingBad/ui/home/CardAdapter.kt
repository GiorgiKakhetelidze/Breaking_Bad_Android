package com.example.breakingBad.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.CharacterItemBinding
import com.example.breakingBad.databinding.LoadingItemBinding
import java.lang.RuntimeException

class CardAdapter(
    var characterList: MutableList<Character>,
    private val onCharacterClick: (characterCard: Character) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var loadingMoreChars = false

    private val onClickListener = View.OnClickListener { v ->
        val card = v?.tag as Character
        onCharacterClick.invoke(card)
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount - 1 == position) HomeFragment.VIEW_TYPE_LOADER else HomeFragment.VIEW_TYPE_CHARACTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            HomeFragment.VIEW_TYPE_CHARACTER -> CharacterViewHolder(
                binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            HomeFragment.VIEW_TYPE_LOADER -> LoadingViewHolder(
                binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context))
            )
            else -> throw RuntimeException("Unknown ViewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterViewHolder -> {
                val item = characterList[position]
                holder.binding.charNameTxtView.text = item.name
                holder.binding.root.tag = item
                Glide.with(holder.itemView).load(item.img).into(holder.binding.charItemView)
            }

            is LoadingViewHolder -> {
                holder.binding.loaderBar.visibility =
                    if (loadingMoreChars) View.VISIBLE else View.GONE
            }
        }
    }

    override fun getItemCount() = characterList.size + 1


    class CharacterViewHolder(
        val binding: CharacterItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(onClickListener)
        }
    }

    class LoadingViewHolder(val binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}