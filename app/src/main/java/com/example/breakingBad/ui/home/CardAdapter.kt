package com.example.breakingBad.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.CharacterItemBinding
import com.example.breakingBad.databinding.LoadingItemBinding
import com.example.breakingBad.databinding.SavedCharacterItemBinding

import java.lang.RuntimeException

class CardAdapter(
    val creatorClassName: String,
    private val onCharacterClick: (character: Character) -> Unit
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

    //Sets on Specific View in ViewHolder Classes and Calls after click on View
    private val onClickListener = View.OnClickListener { v ->
        val character = v?.tag as Character
        onCharacterClick.invoke(character)
    }

    /* We need to override this as we need to differentiate
       which type viewHolder to be attached
      This is being called from onBindViewHolder() method*/
    override fun getItemViewType(position: Int): Int {
        return if (creatorClassName == HOME || creatorClassName == SEARCH)
            if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_CHARACTER
        else if (creatorClassName == SAVED)
            if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_SAVED
        else
            throw RuntimeException("Unknown ViewType")
    }

    // Invoked by layout manager to create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEW_TYPE_CHARACTER -> CharacterViewHolder(
                binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.context)),
                onClickListener = onClickListener
            )
            VIEW_TYPE_SAVED -> {
                SavedCharacterViewHolder(
                    binding = SavedCharacterItemBinding.inflate(LayoutInflater.from(parent.context)),
                    onClickListener = onClickListener
                )
            }
            VIEW_TYPE_LOADER -> LoadingViewHolder(
                binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context))
            )
            else -> throw RuntimeException("Unknown ViewType")
        }

    // Invoked by layout manager to replace the contents of the views
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterViewHolder -> {
                val item = characterList[position]
                holder.binding.charNameTxtView.text = item.name
                holder.binding.root.tag = item
                Glide.with(holder.itemView).load(item.img).into(holder.binding.charItemView)
            }
            is SavedCharacterViewHolder -> {
                val item = characterList[position]
                holder.binding.savedCharacterNameTextView.text = item.name
                holder.binding.savedCharacterNickNameTextView.text = item.nickname
                holder.binding.savedCharacterPortrayedTextView.text = "by\n" + item.portrayed
                holder.binding.savedCharItemView.tag = item
                Glide.with(holder.itemView).load(item.img).into(holder.binding.savedCharItemView)
            }
            is LoadingViewHolder -> {
                holder.binding.loaderBar.visibility =
                    if (loadingMore) View.VISIBLE else View.GONE
            }
        }
    }

    override fun getItemCount() = characterList.size + 1

    //Spans
    class LoaderSpanSizeLookup(private val adapter: CardAdapter) :
        GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (adapter.creatorClassName == HOME || adapter.creatorClassName == SEARCH)
                if (adapter.itemCount - 1 == position) 2 else 1
            else if (adapter.creatorClassName == SAVED)
                1
            else
                throw RuntimeException("Unknown Adapter Creator Class NAme")
        }
    }

    //ViewHolders
    class CharacterViewHolder(
        val binding: CharacterItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(onClickListener)
        }
    }

    class SavedCharacterViewHolder(
        val binding: SavedCharacterItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.savedCharItemView.setOnClickListener(onClickListener)
        }
    }

    class LoadingViewHolder(val binding: LoadingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val VIEW_TYPE_CHARACTER = 1
        const val VIEW_TYPE_LOADER = 2
        const val VIEW_TYPE_SAVED = 3
        const val HOME = "HomeFragment"
        const val SEARCH = "SearchFragment"
        const val SAVED = "SavedCharactersFragment"
    }
}