package com.mufid.uiable

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat

class GBoardEditText : AppCompatEditText {
    lateinit var imgTypeString: ArrayList<String>
    private var keyBoardInputCallbackListener: KeyBoardInputCallbackListener? = null
    private val COMMIT_CONTENT_CONTENT_URI_KEY =
        "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI"
    private val COMMIT_CONTENT_DESCRIPTION_KEY =
        "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION"
    private val COMMIT_CONTENT_LINK_URI_KEY =
        "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI"
    private val COMMIT_CONTENT_OPTS_KEY =
        "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS"
    private val COMMIT_CONTENT_FLAGS_KEY =
        "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS"

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        imgTypeString = arrayListOf(
            "image/png",
            "image/gif",
            "image/jpeg",
            "image/webp"
        )
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val ic: InputConnection? = super.onCreateInputConnection(outAttrs)
        EditorInfoCompat.setContentMimeTypes(outAttrs, imgTypeString.toTypedArray())
        return InputConnectionCompat.createWrapper(ic!!, outAttrs, callback)
    }

    private val callback: InputConnectionCompat.OnCommitContentListener =
        object : InputConnectionCompat.OnCommitContentListener {
            override fun onCommitContent(
                inputContentInfo: InputContentInfoCompat,
                flags: Int, opts: Bundle?
            ): Boolean {
                // read and display inputContentInfo asynchronously
                if (Build.VERSION.SDK_INT >= 25 && flags and InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION != 0) {
                    try {
                        inputContentInfo.requestPermission()
                    } catch (e: java.lang.Exception) {
                        return false // return false if failed
                    }
                } else {
                    val params = Bundle()
                    params.putParcelable(COMMIT_CONTENT_CONTENT_URI_KEY, inputContentInfo.contentUri)
                    params.putParcelable(COMMIT_CONTENT_DESCRIPTION_KEY, inputContentInfo.description)
                    params.putParcelable(COMMIT_CONTENT_LINK_URI_KEY, inputContentInfo.linkUri)
                    params.putInt(COMMIT_CONTENT_FLAGS_KEY, flags)
                    params.putParcelable(COMMIT_CONTENT_OPTS_KEY, opts)
                }
                var supported = false

                for (mimeType in imgTypeString) {
                    if (inputContentInfo.description.hasMimeType(mimeType)) {
                        supported = true
                        break
                    }
                }
                if (!supported) {
                    return false
                }
                if (keyBoardInputCallbackListener != null) {
                    keyBoardInputCallbackListener?.onCommitContent(inputContentInfo, flags, opts)
                }
                return true // return true if succeeded
            }
        }

    interface KeyBoardInputCallbackListener {
        fun onCommitContent(inputContentInfo: InputContentInfoCompat?, flags: Int, opts: Bundle?)
    }

    fun setKeyBoardInputCallbackListener(keyBoardInputCallbackListener: KeyBoardInputCallbackListener?) {
        this.keyBoardInputCallbackListener = keyBoardInputCallbackListener
    }
}