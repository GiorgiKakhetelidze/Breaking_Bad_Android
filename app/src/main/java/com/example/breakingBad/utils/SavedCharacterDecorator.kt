package com.example.breakingBad.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class SavedCharacterDecorator(
    @Dimension
    private val marginStart: Int,
    @Dimension
    private val marginEnd: Int,
    @Dimension
    private val marginTop: Int,
    @Dimension
    private val marginBot: Int,

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
            marginStart,
            marginTop,
            marginEnd,
            if (position != 0 && position == itemCount - 1) marginBot else 0
        )
    }
}