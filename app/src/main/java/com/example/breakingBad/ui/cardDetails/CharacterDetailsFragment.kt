package com.example.breakingBad.ui.cardDetails

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.breakingBad.R
import com.example.breakingBad.base.BaseFragment
import com.example.breakingBad.data.models.character.Character
import com.example.breakingBad.data.models.character.Quote
import com.example.breakingBad.databinding.AppearenceItemBinding
import com.example.breakingBad.databinding.CharacterDetailScreenBinding
import com.example.breakingBad.databinding.QuoteItemBinding
import com.example.breakingBad.ui.login.LoginViewModel
import com.example.breakingBad.utils.observeEvent

class CharacterDetailsFragment : BaseFragment() {

    private var binding: CharacterDetailScreenBinding? = null
    private val characterDetailArg by navArgs<CharacterDetailsFragmentArgs>()
    private val viewModel by viewModels<CharacterDetailsViewModel> {
        CharacterDetailsViewModel.CharacterDetailsViewModelFactory(
            characterDetailArg.characters,
        )
    }
    private val loginViewModel by activityViewModels<LoginViewModel>()

    override fun getViewModelInstance() = viewModel

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

        viewModel.characterModel.observe(viewLifecycleOwner) {
            showCharacterCardData(it)
        }

        binding?.backBtn?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.addRemoveLoginBtn?.setOnClickListener {
            viewModel.buttonClicked()
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
        showAppearance(character)
    }


    @SuppressLint("SetTextI18n")
    private fun showAppearance(character: Character) {
        binding?.apply {
            val betterCallSaulAppearance = character.betterCallSaulAppearance
            val breakingBadAppearance = character.appearance

            if (betterCallSaulAppearance.isNotEmpty()) {
                betterCallSaulAppearance.forEach {
                    val binding = AppearenceItemBinding.inflate(
                        layoutInflater,
                        horizonLinearViewContainer,
                        false
                    )
                    binding.seasonTxtView.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.season_bcs_logo, null)
                    binding.seasonTxtView.text = "\nSEASON\n$it"
                    horizonLinearViewContainer.addView(binding.root)
                }
            }
            if (breakingBadAppearance.isNotEmpty()) {
                breakingBadAppearance.forEach {
                    val binding = AppearenceItemBinding.inflate(
                        layoutInflater,
                        horizonLinearViewContainer,
                        false
                    )
                    binding.seasonTxtView.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.season_bb_logo, null)
                    binding.seasonTxtView.text = "\nSEASON\n$it"
                    horizonLinearViewContainer.addView(binding.root)
                }
            }
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
