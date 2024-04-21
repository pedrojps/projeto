package com.example.myapplication.data.source.local.database.typeConverter

import android.R.attr
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class BitmapTypeConverter {
    @TypeConverter
    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun byteArrayToBitmap(bytes: ByteArray?):Bitmap?{
        //byteArray em Bitmap
        return BitmapFactory.decodeByteArray(bytes,0, bytes?.size ?: 0)
    }
}