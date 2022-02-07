package com.mufid.lib

import android.view.View

interface DraggableListener {
    fun onDraggablePerformedClick(view: View)
    fun onDraggablePositionChanged(view: View)
    fun xDraggableAxisChanged(isInRightSide: Boolean)
}