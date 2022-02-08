package com.mufid.uiable

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.TextureView
import android.widget.FrameLayout
import android.widget.RelativeLayout
import kotlin.math.abs

class Zoomable : FrameLayout {
    var targetTextureView: TextureView? = null
    var transformMatrix: Matrix? = null
    var mode = NONE

    // Remember some things for zooming
    var last: PointF = PointF()
    var start: PointF = PointF()
    var minScale = 1f
    var maxScale = 3f
    var viewWidth = 0
    var viewHeight = 0
    var saveScale = 1f
    private var origWidth = 0f
    private var origHeight = 0f
    var mScaleDetector: ScaleGestureDetector? = null

    constructor(context: Context): super(context) {
        sharedInit(context)
    }

    constructor(context: Context, attr: AttributeSet? = null): super(context, attr) {
        sharedInit(context)
    }

    private fun sharedInit(context: Context){
        super.setClickable(true)
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())

        setOnTouchListener { _, event ->
            if(targetTextureView == null){
                false
            }else {
                mScaleDetector!!.onTouchEvent(event)
                val curr = PointF(event.x, event.y)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        last.set(curr)
                        start.set(last)
                        mode = DRAG
                    }
                    MotionEvent.ACTION_MOVE -> if (mode == DRAG || mode == DRAG_MOVE) {
                        val deltaX: Float = curr.x - last.x
                        val deltaY: Float = curr.y - last.y
                        if((abs(deltaX) > DRAG_MOVE_THRESHOLD || abs(deltaY) > DRAG_MOVE_THRESHOLD) && mode == DRAG){
                            mode = DRAG_MOVE
                        }
                        if (saveScale > 1.0) {
                            transformMatrix?.postTranslate(deltaX, deltaY)
                            targetTextureView?.setTransform(transformMatrix)
                        }
                        // fixTrans()
                        last.set(curr.x, curr.y)
                    }
                    MotionEvent.ACTION_UP -> {
                        mode = NONE
                        val xDiff = abs(curr.x - start.x).toInt()
                        val yDiff = abs(curr.y - start.y).toInt()
                        if (xDiff < CLICK && yDiff < CLICK) {
                            performClick()
                        }
                    }
                    MotionEvent.ACTION_POINTER_UP -> mode = NONE
                }
                invalidate()
                false // indicate event was handled
            }
        }

        setOnLongClickListener {
            if(mode != ZOOM && mode != DRAG_MOVE) {
                transformMatrix?.reset()
                targetTextureView?.setTransform(transformMatrix)
                saveScale = 1.0f
                (parent as? RelativeLayout)?.performLongClick()
                true
            }else {
                false
            }
        }
    }

    fun setTarget(target: TextureView) {
        targetTextureView = target
        transformMatrix = target.matrix
        saveScale = 1.0f
    }

    fun resetTarget() {
        targetTextureView = null
        transformMatrix = null
        saveScale = 1.0f
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
        }
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) transformMatrix?.postScale(mScaleFactor, mScaleFactor, (viewWidth / 2).toFloat(), (viewHeight / 2).toFloat()) else transformMatrix?.postScale(mScaleFactor, mScaleFactor, detector.focusX, detector.focusY)
            if(saveScale <= 1.0f){
                transformMatrix?.reset()
                saveScale = 1.0f
            }
            targetTextureView?.setTransform(transformMatrix)
            return true
        }
    }

    companion object {
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        const val CLICK = 3
        const val DRAG_MOVE = 4
        const val DRAG_MOVE_THRESHOLD = 5
    }
}