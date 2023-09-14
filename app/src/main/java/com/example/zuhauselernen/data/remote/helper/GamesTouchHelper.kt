package com.example.zuhauselernen.data.remote.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class GamesTouchHelper (
    removeMeal: (position: Int) -> Unit
) : ItemTouchHelper(
    object : SimpleCallback(
        UP or DOWN, LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            removeMeal(position)
        }
    })