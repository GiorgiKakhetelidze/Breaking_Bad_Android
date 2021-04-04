package com.example.breakingBad.ui.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Episode
import com.example.breakingBad.databinding.*

import java.lang.RuntimeException

class CardAdapter(
    val creatorClassName: String,
    private val onCharacterClick: (character: Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList: List<Character> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var episodeList: List<Episode> = emptyList()
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
        when (v?.tag) {
            is Character -> {
                val character = v.tag as Character
                onCharacterClick.invoke(character)
            }
            is String -> {
                val str = v.tag as String
                onCharacterClick.invoke(str)
            }
            else -> throw RuntimeException("ClickListener is not specified")
        }
    }

    /* We need to override this as we need to differentiate
       which type viewHolder to be attached
      This is being called from onBindViewHolder() method*/
    override fun getItemViewType(position: Int): Int {
        return if (creatorClassName == HOME || creatorClassName == SEARCH)
            if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_CHARACTER
        else if (creatorClassName == SAVED)
            if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_SAVED
        else if (creatorClassName == SEASON)
            if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_SEASON
        else if (creatorClassName == APPEARANCE)
            if (itemCount - 1 == position) VIEW_TYPE_LOADER else VIEW_TYPE_APPEARANCE
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
            VIEW_TYPE_SEASON -> {
                SeasonViewHolder(
                    binding = SeasonEpisodeItemBinding.inflate(LayoutInflater.from(parent.context)),
                    //onClickListener = onClickListener
                )
            }
            VIEW_TYPE_APPEARANCE -> {
                SeasonAppearanceHolder(
                    binding = AppearenceItemBinding.inflate(LayoutInflater.from(parent.context)),
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
            is SeasonViewHolder -> {
                val item = episodeList[position]
                holder.binding.episodeNumTextView.text = "Episodes" + item.season.toString()
                holder.binding.episodeNameTextView.text = item.title
                if (item.title == "Pilot")
                    Glide.with(holder.itemView).load(R.drawable.season_pilot)
                        .into(holder.binding.episodeItemView)
                else
                    Glide.with(holder.itemView).load(R.drawable.season_img_std)
                        .into(holder.binding.episodeItemView)
            }
            is LoadingViewHolder -> {
                holder.binding.loaderBar.visibility =
                    if (loadingMore) View.VISIBLE else View.GONE
            }
        }
    }

    override fun getItemCount() : Int {
        return when {
            episodeList.isNotEmpty() -> episodeList.size + 1
            characterList.size > 1 || characterList.isEmpty() -> characterList.size + 1
            characterList.first().betterCallSaulAppearance.isNotEmpty() && characterList.first().appearance.isEmpty() -> characterList.first().betterCallSaulAppearance.size + 1
            else -> characterList.first().appearance.size + 1
        }
    }


    //Spans
    class LoaderSpanSizeLookup(private val adapter: CardAdapter) :
        GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (adapter.creatorClassName == HOME || adapter.creatorClassName == SEARCH)
                if (adapter.itemCount - 1 == position) 2 else 1
            else if (adapter.creatorClassName == SAVED || adapter.creatorClassName == APPEARANCE)
                1
            else
                throw RuntimeException("Unknown Adapter Creator Class Name")
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

    class SeasonAppearanceHolder(
        val binding: AppearenceItemBinding,
        onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener(onClickListener)
        }
    }

    class SeasonViewHolder(
        val binding: SeasonEpisodeItemBinding,
        // onClickListener: View.OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        /*    init {
                binding.root.setOnClickListener(onClickListener)
            }*/
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
        const val VIEW_TYPE_SEASON = 4
        const val VIEW_TYPE_APPEARANCE = 5
        const val HOME = "HomeFragment"
        const val SEARCH = "SearchFragment"
        const val SAVED = "SavedCharactersFragment"
        const val SEASON = "SeasonFragment"
        const val APPEARANCE = "CharacterDetailsFragment"
    }
}