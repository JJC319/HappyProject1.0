package cjj.com.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

/**
 * Created by cjj on 16/10/19 下午8:02.
 * Description:带有水纹波纹效果的Button
 */

public class RippleViewButton extends Button {
    private PointF touchPoint = new PointF();
    private int radius = 150;
    private Paint mPaint;
    private ObjectAnimator objectAnimator;

    public RippleViewButton(Context context) {
        super(context);
        init();
    }

    public RippleViewButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RippleViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAlpha(100);
        mPaint.setColor(Color.GREEN);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchPoint.x = event.getX();
                touchPoint.y = event.getY();
                radius = 150;
                setRadius(150);
                break;
            case MotionEvent.ACTION_MOVE:
                touchPoint.x = event.getX();
                touchPoint.y = event.getY();
                setRadius(150);
                break;
            case MotionEvent.ACTION_UP:
                objectAnimator = ObjectAnimator.ofFloat(this, "radius", 150, 1000).setDuration(400);
                objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setRadius(0);
                    }
                });
                objectAnimator.start();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据给定的color和alpha值得到给定的color
     */
    public int getColor(int color, float aAlpha) {
        int alpha = Math.round(Color.alpha(color) * aAlpha);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 在执行ObjectAnimator的过程中就会调用此方法
     */
    public void setRadius(float radius) {
        System.out.println("setRadius----------" + radius);
        this.radius = (int) radius;
        if (this.radius > 0) {
            RadialGradient mRadialGradient = new RadialGradient(touchPoint.x, touchPoint.y, this.radius, getColor(
                    Color.GREEN, 0.1f), Color.GREEN, Shader.TileMode.MIRROR);
            mPaint.setShader(mRadialGradient);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(touchPoint.x, touchPoint.y, radius, mPaint);
        super.onDraw(canvas);
    }
}

