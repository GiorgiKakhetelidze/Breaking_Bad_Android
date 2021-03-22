package com.example.breakingBad.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class CharacterDecorator(
    @Dimension
    private val characterHorizontalInsetSpace: Int,
    @Dimension
    private val characterHorizontalSpacing: Int,
    @Dimension
    private val characterVerticalSpacing: Int,
    @Dimension
    private val bottomTopFragmentSpace: Int

    ) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        outRect.set(
            if (position % 2 == 0) characterHorizontalSpacing else characterHorizontalInsetSpace/2,
            if (position == 0 || position == 1) outRect.top else characterVerticalSpacing,
            if (position % 2 == 0) characterHorizontalInsetSpace/2 else characterHorizontalSpacing,
            if (position == itemCount - 1 || position == itemCount - 2) bottomTopFragmentSpace else 0
        )
    }
}