package com.example.breakingBad.ui.season

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Episode
import com.example.breakingBad.databinding.SeasonScreenBinding
import com.example.breakingBad.ui.cardDetails.CharacterDetailsViewModel
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.utils.SavedCharacterDecorator
import com.example.breakingBad.utils.observeEvent
import java.lang.RuntimeException

class SeasonFragment : BaseFragment() {

    private var binding: SeasonScreenBinding? = null
    private val seasonInfo by navArgs<SeasonFragmentArgs>()
    private val viewModel by viewModels<SeasonViewModel>() {
        SeasonViewModel.SeasonsViewModelFactory(
            seasonInfo.str,
        )
    }

    override fun getViewModelInstance(): BaseViewModel = viewModel

    private var adapter = CardAdapter("SeasonFragment") {
/*        if(it is String){
            val action = SeasonFragmentDirections.actionGlobalSeasonFragment(it)
            activity?.findNavController(R.id.mainContainer)?.navigate(action)
        }else throw RuntimeException("Unknown argument type for SeasonFragment")*/
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
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding?.apply {
            recycleView.layoutManager = layoutManager
            recycleView.adapter = adapter

            setSeasonTitle()

            swipeToRefresh.setOnRefreshListener {
                viewModel.onRefresh()
                binding?.swipeToRefresh?.isRefreshing = false
            }

            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        viewModel.loginRequired.observeEvent(viewLifecycleOwner) {
            activity?.findNavController(R.id.mainContainer)?.navigate(R.id.login)
        }

        viewModel.episodes.observe(viewLifecycleOwner) {
            adapter.episodeList = it
            binding?.swipeToRefresh?.isRefreshing = false
        }
    }

    private fun setSeasonTitle() {
        val infoLst = seasonInfo.str.split("|")
        val seasonTxt = "Season "
        if (infoLst.size == 2) {
            val seasonTitle = seasonTxt + infoLst[1]
            binding?.seasonTitleTxtView?.text = seasonTitle
        } else
            binding?.seasonTitleTxtView?.text = seasonTxt

    }

}