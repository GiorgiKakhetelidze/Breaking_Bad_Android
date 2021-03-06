package com.example.breakingBad.ui.savedCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.SavedCharactersScreenBinding
import com.example.breakingBad.ui.characterDetails.CharacterDetailsFragmentDirections
import com.example.breakingBad.ui.home.HomeAdapter
import com.example.breakingBad.ui.login.LoginViewModel
import com.example.breakingBad.utils.SavedAndEpisodeCharacterDecorator
import com.example.breakingBad.utils.observeEvent
import java.lang.RuntimeException

class SavedCharactersFragment : BaseFragment() {

    private val viewModel by viewModels<SavedCharactersViewModel>()
    override fun getViewModelInstance(): BaseViewModel = viewModel
    private var binding: SavedCharactersScreenBinding? = null
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var adapter = SavedAdapter {
        val action = CharacterDetailsFragmentDirections.actionGlobalCharacterDetailsFragment(it)
        activity?.findNavController(R.id.mainContainer)?.navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavedCharactersScreenBinding.inflate(inflater, container, false)
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
            swipeToRefresh.setOnRefreshListener {
                viewModel.onRefresh()
                swipeToRefresh.isRefreshing = false
            }

        }

        viewModel.requestLogin.observeEvent(viewLifecycleOwner) {
            login()
        }

        viewModel.characters.observe(viewLifecycleOwner) {
            adapter.characterList = it
            binding?.swipeToRefresh?.isRefreshing = false
        }

        loginViewModel.loginFlowFinished.observeEvent(viewLifecycleOwner) { loginSuccess ->
            if (loginSuccess)
                viewModel.getSavedCharacters()
            else
                findNavController().navigate(R.id.show_home)
        }
    }

    private fun login() {
        activity?.findNavController(R.id.mainContainer)?.navigate(R.id.login)
    }
}