package com.example.breakingBad.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.CharacterItemBinding
import com.example.breakingBad.databinding.LoadingItemBinding
import java.lang.RuntimeException

class CardAdapter(
    private val onCharacterClick: (characterCard: Character) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList: List<Character> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var loadingMore = false
        set(value) {
            field = value
            notifyItemChanged(itemCount - 1)
        }

    private val onClickListener = View.OnClickListener { v ->
        val card = v?.tag as Character
        onCharacterClick.invoke(card)
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_CHARACTER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_CHARACTER -> CharacterViewHolder(
                binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            VIEW_TYPE_LOADER -> LoadingViewHolder(
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
                    if (loadingMore) View.VISIBLE else View.GONE
            }
        }
    }

    override fun getItemCount() = characterList.size + 1

    class LoaderSpanSizeLookup(val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) :
        GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (adapter.itemCount - 1 == position) 2 else 1
        }
    }

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

    companion object {
        const val VIEW_TYPE_CHARACTER = 1
        const val VIEW_TYPE_LOADER = 2
    }
}