package com.mufid.draggable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.inputmethod.InputContentInfoCompat
import com.mufid.uiable.GBoardEditText
import android.provider.MediaStore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.io.InputStream


class GBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gboard)

        val editText = findViewById<GBoardEditText>(R.id.edit_text)
        val imageView = findViewById<ImageView>(R.id.image_view)

        editText.setKeyBoardInputCallbackListener(object : GBoardEditText.KeyBoardInputCallbackListener {
            override fun onCommitContent(
                inputContentInfo: InputContentInfoCompat?,
                flags: Int,
                opts: Bundle?
            ) {
                // Uri : inputContentInfo?.contentUri
                val imageUri = inputContentInfo?.contentUri
                val `is`: InputStream? = imageUri?.let { contentResolver.openInputStream(it) }
                val bitmap = BitmapFactory.decodeStream(`is`)
                `is`?.close()

                imageView.setImageBitmap(bitmap)
            }

        })
    }
}