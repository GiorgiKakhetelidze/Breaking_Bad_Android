package com.example.breakingBad.ui.savedCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.base.BaseViewModel
import com.example.breakingBad.databinding.SavedCharactersScreenBinding
import com.example.breakingBad.ui.cardDetails.CharacterDetailsFragmentDirections
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.ui.login.LoginViewModel
import com.example.breakingBad.utils.CharacterDecorator
import com.example.breakingBad.utils.observeEvent

class SavedCharactersFragment : BaseFragment() {

    private val viewModel by viewModels<SavedCharactersViewModel>()
    override fun getViewModelInstance(): BaseViewModel = viewModel
    private var binding : SavedCharactersScreenBinding? = null
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var adapter = CardAdapter() {
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
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = CardAdapter.LoaderSpanSizeLookup(adapter)
        binding?.apply {
            recycleView.layoutManager = layoutManager
            recycleView.adapter = adapter
            recycleView.addItemDecoration(
                CharacterDecorator(
                    characterHorizontalInsetSpace = resources.getDimensionPixelSize(R.dimen.character_horizontal_inset_space),
                    characterHorizontalSpacing = resources.getDimensionPixelSize(R.dimen.character_horizontal_spacing_home),
                    characterVerticalSpacing = resources.getDimensionPixelSize(R.dimen.character_vertical_space),
                    bottomTopFragmentSpace = resources.getDimensionPixelSize(R.dimen.bottom_top_fragment_space)
                )
            )
        }
        viewModel.requestLogin.observeEvent(viewLifecycleOwner) {
            login()
        }
        loginViewModel.loginFlowFinished.observeEvent(viewLifecycleOwner) { loginSuccess ->
            if (loginSuccess)
                viewModel.getSavedCards()
            else
                findNavController().navigate(R.id.show_home)
        }
    }

    private fun login() {
        activity?.findNavController(R.id.mainContainer)?.navigate(R.id.login)
    }
}