/*
 * Copyright 2015 Wicresoft, Inc. All rights reserved.
 */

package com.wise.common.compressorutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;


/**
 * Description of String2Bitmap
 *
 * @author bob.bop
 * @version $Id:$
 * @created on 2015-8-13
 */
public class BitmapFormart {
    /**
     * 将字符串转换成Bitmap类型
     *
     * @param string
     * @return
     */
    public static Bitmap getBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            Log.e("exception", "exception: " + Log.getStackTraceString(e));
        }
        return bitmap;
    }

    /**
     * 将Bitmap转换成字符串类型
     *
     * @return
     */
    public static String getString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        string = MyBase64.encode(baos.toByteArray());
        return string;
    }
}
