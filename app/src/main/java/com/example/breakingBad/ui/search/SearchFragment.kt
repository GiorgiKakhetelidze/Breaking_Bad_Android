package com.example.breakingBad.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.SearchScreenBinding
import com.example.breakingBad.ui.characterDetails.CharacterDetailsFragmentDirections
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.utils.CharacterDecorator
import java.lang.RuntimeException

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private var binding: SearchScreenBinding? = null
    private val adapter = CardAdapter("SearchFragment") {
        if (it is Character) {
            val action = CharacterDetailsFragmentDirections.actionGlobalCharacterDetailsFragment(it)
            activity?.findNavController(R.id.mainContainer)?.navigate(action)
        } else throw RuntimeException("Unknown argument type for DetailsFragment")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = LoaderSpanSizeLookup()
        binding?.recycleView?.layoutManager = layoutManager
        binding?.recycleView?.adapter = adapter
        binding?.recycleView?.addItemDecoration(
            CharacterDecorator(
                characterHorizontalInsetSpace = resources.getDimensionPixelSize(R.dimen.character_horizontal_inset_space),
                characterHorizontalSpacing = resources.getDimensionPixelSize(R.dimen.character_horizontal_spacing_search),
                characterVerticalSpacing = resources.getDimensionPixelSize(R.dimen.character_vertical_space),
                bottomTopFragmentSpace = resources.getDimensionPixelSize(R.dimen.bottom_top_fragment_space)
            )
        )
        viewModel.characters.observe(viewLifecycleOwner) {
            adapter.characterList = it
        }

        binding?.searchInput?.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchTextChange(text)
        }
    }

    inner class LoaderSpanSizeLookup : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (adapter.itemCount - 1 == position) 2 else 1
        }
    }

}