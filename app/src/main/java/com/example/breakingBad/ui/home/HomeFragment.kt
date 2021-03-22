package com.example.breakingBad.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.showDialog
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.network.NetworkClient
import com.example.breakingBad.databinding.*
import com.example.breakingBad.ui.cardDetails.CharacterDetailsFragment
import com.example.breakingBad.utils.CharacterDecorator
import com.example.breakingBad.utils.LoadMoreListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeFragment : Fragment() {

    private val binding by lazy { HomeScreenBinding.inflate(layoutInflater) }
    private var characterList = mutableListOf<Character>()
    private var noMoreCharacters = false
    private val adapter = CardAdapter(characterList) {
        activity?.findNavController(R.id.mainContainer)?.navigate(
            R.id.showCharacterDetails,
            bundleOf(CharacterDetailsFragment.KEY_DATA to it)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = LoaderSpanSizeLookup()
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
        binding.recycleView.addItemDecoration(
            CharacterDecorator(
                characterHorizontalInsetSpace = resources.getDimensionPixelSize(R.dimen.character_horizontal_inset_space),
                characterHorizontalSpacing = resources.getDimensionPixelSize(R.dimen.character_horizontal_spacing_home),
                characterVerticalSpacing = resources.getDimensionPixelSize(R.dimen.character_vertical_space),
                bottomTopFragmentSpace = resources.getDimensionPixelSize(R.dimen.bottom_top_fragment_space)
            )
        )

        binding.recycleView.addOnScrollListener(LoadMoreListener(this::loadCharacters))

        binding.swipeToRefresh.setOnRefreshListener {
            if (adapter.loadingMoreChars) binding.swipeToRefresh.isRefreshing = false
            characterList.clear()
            adapter.notifyDataSetChanged()
            noMoreCharacters = false
            loadCharacters()
        }
        loadCharacters()
    }

    private fun loadCharacters() {
        if (adapter.loadingMoreChars || noMoreCharacters) return
        lifecycleScope.launchWhenStarted {
            adapter.notifyItemChanged(adapter.itemCount - 1)
            adapter.loadingMoreChars = true
            try {
                val data = withContext(Dispatchers.IO) {
                    NetworkClient.characterService.getLimitedCharacters(
                        LIMIT,
                        adapter.itemCount - 1
                    )
                }
                characterList.addAll(data)
                noMoreCharacters = data.size != LIMIT
                adapter.notifyItemRangeInserted(characterList.size - 1, data.size)
            } catch (e: Exception) {
                showDialog(R.string.common_error, e.message ?: "")
            } finally {
                adapter.loadingMoreChars = false
                binding.swipeToRefresh.isRefreshing = false
            }
        }
    }

    inner class LoaderSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (adapter.itemCount - 1 == position) 2 else 1
        }
    }

    companion object {
        const val VIEW_TYPE_CHARACTER = 1
        const val VIEW_TYPE_LOADER = 2
        const val LIMIT = 10
    }
}