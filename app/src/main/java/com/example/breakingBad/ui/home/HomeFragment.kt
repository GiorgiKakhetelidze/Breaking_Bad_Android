package com.example.breakingBad.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.databinding.*
import com.example.breakingBad.ui.characterDetails.CharacterDetailsFragmentDirections
import com.example.breakingBad.utils.CharacterDecorator
import com.example.breakingBad.utils.LoadMoreListener

class HomeFragment : BaseFragment() {

    private var binding: HomeScreenBinding? = null
    private val viewModel by viewModels<HomeViewModel>()

    override fun getViewModelInstance() = viewModel

    private val adapter = HomeAdapter{
        val action = CharacterDetailsFragmentDirections.actionGlobalCharacterDetailsFragment(it)
        activity?.findNavController(R.id.mainContainer)?.navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = HomeAdapter.LoaderSpanSizeLookup(adapter)
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

            recycleView.addOnScrollListener(LoadMoreListener() {
                viewModel.onScrollEndReached()
            })

            swipeToRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }

            viewModel.characters.observe(viewLifecycleOwner) {
                adapter.characterList = it
            }

            viewModel.loadingMore.observe(viewLifecycleOwner) {
                adapter.loadingMore = it
                if (swipeToRefresh.isRefreshing && it) swipeToRefresh.isRefreshing = false
            }
        }
    }

}