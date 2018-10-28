package com.dertyp7214.themeablecomponents.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.Random;

public class Utils {

    private static Random random = new Random();

    public static Drawable drawableToBitmapToDrawable(Context context, Drawable drawable, DrawableCallback callback) {
        return new BitmapDrawable(context.getResources(),
                callback.process(((BitmapDrawable) drawable).getBitmap()));
    }

    public static String getIdFromView(View v) {
        int id = v.getId();                       // get integer id of view
        String idString = "no_id" + random.nextInt();
        if (id != View.NO_ID) {                    // make sure id is valid
            Resources res = v.getResources();     // get resources
            if (res != null)
                idString = res.getResourceEntryName(id); // get id string entry
        }
        return idString;
    }

    static interface DrawableCallback {
        Bitmap process(Bitmap bitmap);
    }
}
