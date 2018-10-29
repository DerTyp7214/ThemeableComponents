package com.dertyp7214.themeablecomponents.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.util.*

object Utils {

    private val random = Random()

    fun drawableToBitmapToDrawable(context: Context, drawable: Drawable, callback: DrawableCallback): Drawable {
        return BitmapDrawable(context.resources,
                callback.process((drawable as BitmapDrawable).bitmap))
    }

    fun getIdFromView(v: View): String {
        val id = v.id                       // get integer id of view
        var idString = "no_id" + random.nextInt()
        if (id != View.NO_ID) {                    // make sure id is valid
            val res = v.resources     // get resources
            if (res != null)
                idString = res.getResourceEntryName(id) // get id string entry
        }
        return idString
    }

    interface DrawableCallback {
        fun process(bitmap: Bitmap): Bitmap
    }
}
