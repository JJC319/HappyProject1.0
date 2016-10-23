package com.cjj.commonutils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by cjj on 2016/10/21 上午9:49.
 * Description:
 */

public class BitmapUtil {

    public final static void recycleImageView(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = (Drawable) imageView.getDrawable();
            if (drawable != null) {
                drawable.setCallback(null);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageView.setBackground(null);
            } else {
                imageView.setBackgroundDrawable(null);
            }
        }
    }

    public final static void recycleButton(Button button) {
        if (button != null) {
            Drawable drawable = (Drawable) button.getBackground();
            if (drawable != null) {
                drawable.setCallback(null);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                button.setBackground(null);
            } else {
                button.setBackgroundDrawable(null);
            }
        }
    }

    /**
     * Bitmap 转换成 字节数组
     *
     * @param bmp
     * @param needRecycle 是否回收Bitmap
     * @return
     */
    public final static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 回收ImageView占用的图像内存;
     *
     * @param view
     */
    public static void recycleImageViewWithBitMap(View view) {
        if (view == null) return;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    bmp = null;
                }
            }
        }
    }
}
