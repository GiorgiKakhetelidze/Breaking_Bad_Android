package com.example.breakingBad.ui.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.databinding.SavedCharactersScreenBinding
import com.example.breakingBad.databinding.SeasonScreenBinding
import com.example.breakingBad.ui.cardDetails.CharacterDetailsFragmentDirections
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.utils.SavedCharacterDecorator

class SeasonFragment : BaseFragment() {

    private val viewModel by viewModels<SeasonViewModel>()
    private var binding: SeasonScreenBinding? = null

    override fun getViewModelInstance(): BaseViewModel = viewModel

    private var adapter = CardAdapter("SeasonFragment") {
        val action = SeasonFragmentDirections.actionGlobalSeasonFragment()
        activity?.findNavController(R.id.mainContainer)?.navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SeasonScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 1)
        layoutManager.spanSizeLookup = CardAdapter.LoaderSpanSizeLookup(adapter)
        binding?.apply {
            recycleView.layoutManager = layoutManager
            recycleView.adapter = adapter
            recycleView.addItemDecoration(
                SavedCharacterDecorator(
                    marginStart = resources.getDimensionPixelSize(R.dimen.saved_character_margin_start),
                    marginEnd = resources.getDimensionPixelSize(R.dimen.saved_character_margin_end),
                    marginTop = resources.getDimensionPixelSize(R.dimen.saved_character_margin_top),
                    marginBot = resources.getDimensionPixelSize(R.dimen.saved_character_margin_bot)
                )
            )

        }
    }


}