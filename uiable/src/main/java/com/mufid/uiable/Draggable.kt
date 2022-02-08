package com.mufid.uiable

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Draggable(context: Context, private val attrs: AttributeSet) :
    RelativeLayout(context, attrs) {

    private var draggableListener: DraggableListener? = null
    private var widgetInitialX: Float = 0F
    private var widgetDX: Float = 0F
    private var widgetInitialY: Float = 0F
    private var widgetDY: Float = 0F
    var shouldSnap:Boolean = false
    var dragStyle: DragStyle? = null

    init {
        draggableSetup()
    }

    @SuppressLint("Recycle")
    private fun draggableSetup() {
        val style = context.obtainStyledAttributes(attrs, R.styleable.Draggable)
        shouldSnap = style.getBoolean(R.styleable.Draggable_clickable_support, false)
        val draggableStyle = style.getString(R.styleable.Draggable_draggable_style)
        dragStyle = DragStyle.getById(draggableStyle?.toInt() ?: 0)

        this.setOnTouchListener { v, event ->
            val viewParent = v.parent as View
            val parentHeight = viewParent.height
            val parentWidth = viewParent.width
            val xMax = parentWidth - v.width
            val xMiddle = parentWidth / 2
            val yMax = parentHeight - v.height
            val yMiddle = parentHeight / 2

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
                    when (dragStyle) {
                        DragStyle.STICKY_X -> {
                            if (event.rawX >= xMiddle) {
                                v.animate().x(xMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            } else {
                                v.animate().x(0F).setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            }
                        }
                        DragStyle.STICKY_Y -> {
                            if (event.rawY >= yMiddle) {
                                v.animate().y(yMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            } else {
                                v.animate().y(0F).setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            }
                        }
                        DragStyle.STICKY_XY -> {
                            if (event.rawX >= xMiddle) {
                                v.animate().x(xMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            } else {
                                v.animate().x(0F).setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            }

                            if (event.rawY >= yMiddle) {
                                v.animate().y(yMax.toFloat())
                                    .setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            } else {
                                v.animate().y(0F).setDuration(DURATION_MILLIS)
                                    .setUpdateListener { draggableListener?.onDraggablePositionChanged(v) }
                                    .start()
                            }
                        }
                        else -> { }
                    }
                    if (abs(v.x - widgetInitialX) <= DRAG_TOLERANCE && abs(v.y - widgetInitialY) <= DRAG_TOLERANCE) {
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
        Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show()
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