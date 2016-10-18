package cjj.com.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by cjj on 16/10/18 下午4:21.
 * Description: 圆形或是圆角图片
 */

public class CircleImageView extends ImageView {
    private static final String TAG = "CircleImageView";

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 半径
     */
    private int radius = 0;

    /**
     * 是否是圆形裁剪
     * true圈形，false圆角图片
     */
    private boolean isCircle = true;

    /**
     * 裁剪类型
     */
    private int cropType = CropType.CENTER;

    /**
     * 绘制类型
     */
    private int drawType = DrawType.XFERMODE;

    /**
     * 圈形图片的裁剪位置
     */
    public static final class CropType {
        public static final int LEFT_TOP = 1; //显示图片的左上角部分
        public static final int LEFT_BOTTOM = 2;//显示图片的左下角部分
        public static final int RIGHT_TOP = 3;//显示图片的右上角部分
        public static final int RIGHT_BOTTOM = 4;//显示图片的右下角部分
        public static final int LEFT_CENTER = 5;//显示图片的左居中部分
        public static final int RIGHT_CENTER = 6;//显示图片的右居中部分
        public static final int TOP_CENTER = 7;//显示图片的上居中部分
        public static final int BOTTOM_CENTER = 8;//显示图片的下居中部分
        public static final int CENTER = 9;//显示图片的居中部分
    }

    /**
     * 使用BitmapShaper方式绘制还是Xfermode的方式绘制
     */
    public static final class DrawType {
        public static final int SHADER = 1; //使用BitmapShaper方式绘制
        public static final int XFERMODE = 2; //使用Xfermode方式绘制
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = null;
        try {
            array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);
            int count = array.getIndexCount();
            //Log.d(TAG, "count = " + count);
            for (int i = 0; i < count; i++) {
                final int index = array.getIndex(i);
                if (index == R.styleable.CircleImageView_radius) {
                    radius = array.getDimensionPixelSize(index, 0); //初始化半径
                } else if (index == R.styleable.CircleImageView_cropType) {
                    cropType = array.getInteger(index, CropType.CENTER); //初始化截取类型
                } else if (index == R.styleable.CircleImageView_drawType) {
                    drawType = array.getInteger(index, DrawType.XFERMODE); //初始化绘制类型
                } else if (index == R.styleable.CircleImageView_iscircle) {
                    isCircle = array.getBoolean(index, true); //是否绘制圆
                }
            }
            //Log.d(TAG, "radius = " + radius + " cropType = " + cropType + " drawType = " + drawType + " isCircle = " + isCircle);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int intrinsicWidth = getDrawable().getIntrinsicWidth();
        int intrinsicHeight = getDrawable().getIntrinsicHeight();
        Log.d(TAG, "intrinsicWidth = " + intrinsicWidth + " intrinsicHeight = " + intrinsicHeight);
        if (isCircle) {
            /**
             *1、如果圆形半径设置为0，则使用图片中宽高之间的最小值作为圆形的半径
             *2、如果圆形半径不为0，则取半径，宽，高之间的最小值作为半径
             **/
            int width = resolveAdjustedSize(radius == 0 ? intrinsicWidth : Math.min(intrinsicWidth, radius * 2), Integer.MAX_VALUE, widthMeasureSpec);
            int height = resolveAdjustedSize(radius == 0 ? intrinsicHeight : Math.min(intrinsicHeight, radius * 2), Integer.MAX_VALUE, heightMeasureSpec);

            int border = Math.min(width, height);

            radius = border / 2;

            //Log.d(TAG, "isCircle border = " + border + " radius = " + radius);
            setMeasuredDimension(border, border);
        } else {
            /**
             *圆角图片的圆角半径取半径，宽，高之间的最小值
             **/
            int width = resolveAdjustedSize(intrinsicWidth, Integer.MAX_VALUE, widthMeasureSpec);
            int height = resolveAdjustedSize(intrinsicHeight, Integer.MAX_VALUE, heightMeasureSpec);
            radius = Math.min(Math.min(width, height), radius);
            //Log.d(TAG, "isCircle not border = " + Math.min(width, height) + " radius = " + radius);
            setMeasuredDimension(width, height);
        }
    }

    /**
     * 设置半径
     **/
    public void setRadius(int radius) {
        this.radius = radius;
        requestLayout();
    }

    /**
     * 设置截取类型
     **/
    public void setCropType(int cropType) {
        this.cropType = cropType;
        invalidate();
    }

    /**
     * 设置绘制类型
     **/
    public void setDrawType(int drawType) {
        this.drawType = drawType;
        invalidate();
    }

    /**
     * 此方法参考了ImageView的resolveAdjustedSize方法，可以自己查阅
     **/
    private int resolveAdjustedSize(int desiredSize, int maxSize,
                                    int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        if (drawType == DrawType.XFERMODE) {
            drawByXfermode(canvas); //使用Xfermode方式绘制
        } else {
            drawByShader(canvas); //使用Bitmap方式绘制
        }
    }

    private void drawByXfermode(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int restore = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);  //保存Layer
        if (isCircle) {
            canvas.drawCircle(radius, radius, radius, paint); //绘制圆形

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //设置Xfermode
            canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG); //二次保存Layer

            Bitmap bitmap = drawableToBitmap(getDrawable());
            int[] xy = getCropTypeCircleXY(bitmap); //获取图片中显示圆形的中心坐标
            Rect src = new Rect(); //定义设置源图片的显示区域
            src.left = xy[0] - radius;
            src.right = xy[0] + radius;
            src.top = xy[1] - radius;
            src.bottom = xy[1] + radius;
            canvas.drawBitmap(bitmap, src, new Rect(0, 0, width, height), null); //绘制图片
        } else {
            RectF rect = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rect, radius, radius, paint); //绘制圆角矩形
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置Xfermode
            canvas.saveLayer(0, 0, width, height, paint, Canvas.ALL_SAVE_FLAG);//二次保存Layer
            Bitmap bitmap = drawableToBitmap(getDrawable());
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
        canvas.restoreToCount(restore); //恢复Layer
        paint.setXfermode(null);
    }

    private void drawByShader(Canvas canvas) {
        Bitmap src = drawableToBitmap(getDrawable());
        if (isCircle) {
            int[] cropTypeCircleXY = getCropTypeCircleXY(src);//获取图片中显示圆形的中心坐标
            Bitmap bitmap = Bitmap.createBitmap(src, cropTypeCircleXY[0] - radius, cropTypeCircleXY[1] - radius, getWidth(), getHeight()); //创建显示区域的图片
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP); //设置BitmapShader
            paint.setShader(bitmapShader);
            canvas.drawCircle(radius, radius, radius, paint);
        } else {
            BitmapShader bitmapShader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(bitmapShader);
            int width = getWidth();
            int height = getHeight();
            RectF rect = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rect, radius, radius, paint);
        }
    }

    /**
     * 将drawable转换成bitmap
     **/
    private Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获得显示区域的圆形坐标
     **/
    private int[] getCropTypeCircleXY(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] xy;
        switch (cropType) {
            case CropType.LEFT_TOP:
                xy = new int[]{radius, radius};
                break;
            case CropType.LEFT_BOTTOM:
                xy = new int[]{radius, height - radius};
                break;
            case CropType.RIGHT_TOP:
                xy = new int[]{width - radius, radius};
                break;
            case CropType.RIGHT_BOTTOM:
                xy = new int[]{width - radius, height - radius};
                break;
            case CropType.LEFT_CENTER:
                xy = new int[]{radius, height / 2};
                break;
            case CropType.RIGHT_CENTER:
                xy = new int[]{width - radius, height / 2};
                break;
            case CropType.TOP_CENTER:
                xy = new int[]{width / 2, radius};
                break;
            case CropType.BOTTOM_CENTER:
                xy = new int[]{width / 2, height - radius};
                break;
            case CropType.CENTER:
                xy = new int[]{width / 2, height / 2};
                break;
            default:
                xy = new int[]{width / 2, height / 2};
                break;
        }
        return xy;
    }
}
