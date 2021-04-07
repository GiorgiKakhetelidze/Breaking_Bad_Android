package com.example.breakingBad.ui.episodeDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.EpisodeDetailScreenBinding
import com.example.breakingBad.ui.characterDetails.CharacterDetailsFragmentDirections
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.utils.SavedAndEpisodeCharacterDecorator
import java.lang.RuntimeException

class EpisodeDetailsFragment : BaseFragment() {

    override fun getViewModelInstance(): BaseViewModel = viewModel

    private val episodeItem by navArgs<EpisodeDetailsFragmentArgs>()

    private val viewModel by viewModels<EpisodeDetailsViewModel>() {
        EpisodeDetailsViewModel.EpisodeDetailsViewModelFactory(
            episodeItem.episode,
        )
    }

    private var binding: EpisodeDetailScreenBinding? = null

    private var adapter = CardAdapter("EpisodeDetailsFragment") {
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
                SavedAndEpisodeCharacterDecorator(
                    marginStart = resources.getDimensionPixelSize(R.dimen.std_screen_insets),
                    marginEnd = resources.getDimensionPixelSize(R.dimen.std_screen_insets),
                    marginTop = resources.getDimensionPixelSize(R.dimen.std_screen_insets),
                    marginBot = resources.getDimensionPixelSize(R.dimen.screen_inset_bottom)
                )
            )

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

        }

        viewModel.characters.observe(viewLifecycleOwner) {
            showEpisodeData()
            adapter.characterList = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showEpisodeData() = binding?.apply {
        val title = episodeItem.episode.title
        val season = "Season "
        episodeSeasonTxtView.text = season + episodeItem.episode.season.toString()
        episodeTitleTxtView.text = title
        if (title == PILOT_SERIES_LOGO)
            Glide.with(root).load(R.drawable.season_pilot).into(episodeItemImgView)
        else
            Glide.with(root).load(R.drawable.season_img_std).into(episodeItemImgView)

        val series = episodeItem.episode.series
        if (series == BREAKING_BAD)
            Glide.with(root).load(R.drawable.breaking_bad_logo).into(episodeItemLogoView)
        else
            Glide.with(root).load(R.drawable.better_call_saul_logo).into(episodeItemLogoView)
    }

    companion object {
        const val PILOT_SERIES_LOGO = "Pilot"
        const val BREAKING_BAD = "Breaking Bad"
    }

}