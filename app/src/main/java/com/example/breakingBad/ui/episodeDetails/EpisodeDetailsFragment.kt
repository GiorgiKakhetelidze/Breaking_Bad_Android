package com.example.breakingBad.ui.episodeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.databinding.EpisodeDetailScreenBinding
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.ui.season.SeasonViewModel
import com.example.breakingBad.utils.SavedCharacterDecorator

class EpisodeDetailsFragment : BaseFragment() {


    override fun getViewModelInstance(): BaseViewModel = viewModel

    private val episodeItem by navArgs<EpisodeDetailsFragmentArgs>()

    private val viewModel by viewModels<EpisodeDetailsViewModel>() {
        EpisodeDetailsViewModel.EpisodeDetailsViewModelFactory(
            episodeItem.episode,
        )
    }

    private var binding: EpisodeDetailScreenBinding? = null

    private var adapter = CardAdapter("SavedCharactersFragment") {
/*        if (it is Character) {
            val action = CharacterDetailsFragmentDirections.actionGlobalCharacterDetailsFragment(it)
            activity?.findNavController(R.id.mainContainer)?.navigate(action)
        } else throw RuntimeException("Unknown argument type for DetailsFragment")*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EpisodeDetailScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
            swipeToRefresh.setOnRefreshListener {
                //viewModel.onRefresh()
                swipeToRefresh.isRefreshing = false
            }
        }
    }

}