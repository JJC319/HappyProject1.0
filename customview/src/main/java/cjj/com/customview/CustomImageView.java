package cjj.com.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cjj.commonutils.BitmapUtil;

/**
 * Created by cjj on 2016/10/21 上午9:46.
 * Description:
 */

public class CustomImageView extends ImageView{


    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundResource(int resid) {
        if (resid != -1 || resid != 0) {
            BitmapUtil.recycleImageView(this);
            super.setBackgroundResource(resid);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            BitmapUtil.recycleImageView(this);
            super.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImageResource(int resId) {
        if (resId != -1 || resId != 0) {
            BitmapUtil.recycleImageView(this);
            super.setImageResource(resId);
        }
    }

}
