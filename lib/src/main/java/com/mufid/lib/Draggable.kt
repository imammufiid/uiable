package com.mufid.lib

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Draggable(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs) {

    private var draggableListener: DraggableListener? = null
    private var widgetInitialX: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetInitialY: Float = 0F
    private var widgetDY: Float = 0F
    var shouldSnap:Boolean = false

    init {
        draggableSetup()
    }

    private fun draggableSetup() {
        this.setOnTouchListener { v, event ->
            val viewParent = v.parent as View
            val parentHeight = viewParent.height
            val parentWidth = viewParent.width
            val xMax = parentWidth - v.width
            val xMiddle = parentWidth / 2
            val yMax = parentHeight - v.height

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    widgetDX = v.x - event.rawX
                    widgetDY = v.y - event.rawY
                    widgetInitialX = v.x
                    widgetInitialY = v.y
                }
                MotionEvent.ACTION_MOVE -> {
                    var newX = event.rawX + widgetDX
                    newX = max(0F, newX)
                    newX = min(xMax.toFloat(), newX)
                    v.x = newX

                    var newY = event.rawY + widgetDY
                    newY = max(0F, newY)
                    newY = min(yMax.toFloat(), newY)
                    v.y = newY

                    draggableListener?.onDraggablePositionChanged(v)
                }
                MotionEvent.ACTION_UP -> {
                    if(shouldSnap) {
                        if (event.rawX >= xMiddle) {
                            v.animate().x(xMax.toFloat())
                                .setDuration(Draggable.DURATION_MILLIS)
                                .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                .start()
                        } else {
                            v.animate().x(0F).setDuration(Draggable.DURATION_MILLIS)
                                .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                .start()
                        }
                    }
                    if (abs(v.x - widgetInitialX) <= Draggable.DRAG_TOLERANCE && abs(v.y - widgetInitialY) <= Draggable.DRAG_TOLERANCE) {
                        performClick()
                    } else draggableListener?.xDraggableAxisChanged(event.rawX >= xMiddle)
                }
                else -> return@setOnTouchListener false
            }
            true
        }
    }

    override fun performClick(): Boolean {
        this.draggableListener?.onDraggablePerformedClick(this)
        return super.performClick()
    }

    fun setListener(draggableListener: DraggableListener?) {
        this.draggableListener = draggableListener
    }

    companion object {
        const val DRAG_TOLERANCE = 16
        const val DURATION_MILLIS = 250L
    }
}