package com.example.breakingBad.ui.characterDetails

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Quote
import com.example.breakingBad.databinding.CharacterDetailScreenBinding
import com.example.breakingBad.databinding.QuoteItemBinding
import com.example.breakingBad.ui.home.CardAdapter
import com.example.breakingBad.ui.login.LoginViewModel
import com.example.breakingBad.ui.season.SeasonFragmentDirections
import com.example.breakingBad.utils.observeEvent
import java.lang.RuntimeException

class CharacterDetailsFragment : BaseFragment() {

    private var binding: CharacterDetailScreenBinding? = null

    private val characterDetailArg by navArgs<CharacterDetailsFragmentArgs>()

    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun getViewModelInstance() = viewModel

    private val viewModel by viewModels<CharacterDetailsViewModel> {
        CharacterDetailsViewModel.CharacterDetailsViewModelFactory(
            characterDetailArg.character,
        )
    }

    private val adapter = CardAdapter<Character>("CharacterDetailsFragment") {
        if (it is String) {
            val action = SeasonFragmentDirections.actionGlobalSeasonFragment(it)
            activity?.findNavController(R.id.mainContainer)?.navigate(action)
        } else throw RuntimeException("Unknown argument type for SeasonFragment")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CharacterDetailScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.apply {
            recycleView.layoutManager = layoutManager
            adapter.itemList = listOf(characterDetailArg.character)
            recycleView.adapter = adapter
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            addRemoveLoginBtn.setOnClickListener {
                viewModel.buttonClicked()
            }
        }

        viewModel.characterModel.observe(viewLifecycleOwner) {
            showCharacterCardData(it)
        }

        viewModel.characterSaved.observe(viewLifecycleOwner) {
            when (it) {
                CharacterDetailsViewModel.CharacterSavedState.NotSaved -> binding?.addRemoveLoginBtn?.setText(
                    R.string.character_details_save
                )
                CharacterDetailsViewModel.CharacterSavedState.Saved -> binding?.addRemoveLoginBtn?.setText(
                    R.string.character_details_remove
                )
                else -> binding?.addRemoveLoginBtn?.setText(R.string.character_details_login)
            }
        }

        viewModel.showConfirmDialog.observeEvent(viewLifecycleOwner) {
            val dialog =
                AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
                    .setMessage(R.string.are_you_sure_delete)
                    .setPositiveButton(
                        R.string.common_yes
                    ) { dialog, _ ->
                        dialog.dismiss()
                        viewModel.deleteConfirmed()
                    }
                    .setNeutralButton(R.string.common_no) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }.show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.orange, context?.theme))
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setTextColor(resources.getColor(R.color.orange, context?.theme))
        }

        viewModel.loginRequired.observeEvent(viewLifecycleOwner) {
            activity?.findNavController(R.id.mainContainer)?.navigate(R.id.login)
        }

        loginViewModel.loginFlowFinished.observeEvent(viewLifecycleOwner) {
            if (it) viewModel.determineCardSavedState()
        }


        viewModel.quotes.observe(viewLifecycleOwner) {
            showQuotes(it)
        }
    }

    private fun showCharacterCardData(character: Character) {
        binding?.apply {
            Glide.with(imgCharacterView).load(character.img).into(imgCharacterView)

            val nameSurname = character.name.split(" ")
            when (nameSurname.size) {
                1 -> {
                    characterNameTxtView.text = nameSurname[0]
                    characterNameTxtViewSecond.visibility = View.GONE
                    characterSurNameTxtView.visibility = View.GONE
                    characterParticleTxtView.visibility = View.GONE
                }
                3 -> {
                    characterNameTxtView.text = nameSurname[0]
                    characterSurNameTxtView.text = nameSurname[1]
                    characterNameTxtViewSecond.text = nameSurname[2]
                    characterParticleTxtView.visibility = View.GONE
                }
                4 -> {
                    characterNameTxtView.text = nameSurname[0]
                    characterSurNameTxtView.text = nameSurname[1]
                    characterNameTxtViewSecond.text = nameSurname[2]
                    characterParticleTxtView.text = nameSurname[3]
                }
                else -> {
                    characterNameTxtView.text = nameSurname[0]
                    characterSurNameTxtView.text = nameSurname[1]
                    characterNameTxtViewSecond.visibility = View.GONE
                    characterParticleTxtView.visibility = View.GONE
                }
            }

            characterNickNameTitle.text = character.nickname

            var occupationTxt = ""
            for (text in character.occupation) {
                occupationTxt += (text + "\n")
            }
            characterOccupation.text = occupationTxt

            birthdayValue.text = character.birthday
            statusValue.text = character.status
            portrayedValue.text = character.portrayed
        }
    }

    private fun showQuotes(quotes: MutableList<Quote>) = binding?.apply {
        if (quotes.isEmpty()) {
            quotesContainer.visibility = View.GONE
            quotesTitle.visibility = View.GONE
        } else {
            quotesContainer.visibility = View.VISIBLE
            quotesTitle.visibility = View.VISIBLE
            quotesContainer.removeAllViews()

            quotes.forEach {
                val binding = QuoteItemBinding.inflate(layoutInflater, quotesContainer, false)
                binding.quoteTextView.text = it.quote
                quotesContainer.addView(binding.root)
            }
        }
    }

}
