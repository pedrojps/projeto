package com.example.myapplication.utils

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


object ImageUtil {

    fun imageClicle(image: Bitmap, imageView: ImageView) {
        val rounded = RoundedBitmapDrawableFactory.create(imageView.resources, image)
        rounded.isCircular = true
        imageView.setImageDrawable(rounded)
    }

    fun imageClicle(drawable: Drawable?, imageView: ImageView): Bitmap {
        val backgroud = imageView.context.getDrawable(R.drawable.white_backgroud)
        val finalDrawable = LayerDrawable(arrayOf(backgroud,drawable))
        val image = finalDrawable.toBitmap()
        val rounded = RoundedBitmapDrawableFactory.create(imageView.resources, image)
        rounded.isCircular = true
        imageView.setImageDrawable(rounded)
        return image
    }

    fun permissionReadImage(): String {
        return if (Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        }else
            Manifest.permission.READ_EXTERNAL_STORAGE
    }

    fun readBtn(mcoContext: Context, sFileName: String?): Bitmap? {
        val yourFilePath: String = mcoContext.filesDir.toString() + "/saved_images/" + "Image-" + sFileName.toString() + ".jpg"
        val yourFile = File(yourFilePath)

        if (!yourFile.exists()) return null

        return BitmapFactory.decodeStream(FileInputStream(yourFile))
    }

    fun savaImage(mcoContext: Context, @DrawableRes finalInt: Int, sFileName: String?){
        saveImage(mcoContext,mcoContext.getDrawable(finalInt)?.toBitmap(),sFileName)
    }

    fun savaImage(mcoContext: Context, finalDrawable: Drawable?, sFileName: String?){
        saveImage(mcoContext,finalDrawable?.toBitmap(),sFileName)
    }

    fun saveImage(mcoContext: Context, finalBitmap: Bitmap?, sFileName: String?) {
        val root =  mcoContext.filesDir.toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val fname = "Image-" + sFileName.toString() + ".jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        finalBitmap ?: return
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun readSetImage(mcoContext: Context, sFileName: String?, imageView: ImageView){
        val image = readBtn(mcoContext, sFileName)
        if (image != null) {
            imageClicle(image, imageView)
        } else {
            imageClicle(mcoContext.getDrawable(R.mipmap.ic_habit_defult), imageView)
        }
    }
}