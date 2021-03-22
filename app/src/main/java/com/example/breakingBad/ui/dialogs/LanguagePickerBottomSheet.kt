package com.example.breakingBad.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.breakingBad.data.storage.DataStore
import com.example.breakingBad.databinding.LanguagePickerBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguagePickerBottomSheet : BottomSheetDialogFragment() {

    lateinit var binding: LanguagePickerBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LanguagePickerBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.langBtnEng.setOnClickListener {
            DataStore.language = "en"
            dismiss()
            activity?.recreate()
        }
        binding.langBtnGeo.setOnClickListener {
            DataStore.language = "ka"
            dismiss()
            activity?.recreate()
        }
    }
}