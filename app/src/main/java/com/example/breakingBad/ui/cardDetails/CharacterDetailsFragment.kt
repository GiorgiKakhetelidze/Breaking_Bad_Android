package com.example.breakingBad.ui.cardDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.databinding.CharacterDetailScreenBinding

class CharacterDetailsFragment : Fragment() {
    private var binding: CharacterDetailScreenBinding? = null
    private val characterDetailArg by lazy { arguments?.getParcelable<Character>(KEY_DATA) }
    private val viewModel by viewModels<CharacterDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setCardData(characterDetailArg!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharacterDetailScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.characterModel.observe(viewLifecycleOwner, this::showCharacterCardData)
    }

    private fun showCharacterCardData(character : Character){

    }

    companion object {
        const val KEY_DATA = "data"
    }
}