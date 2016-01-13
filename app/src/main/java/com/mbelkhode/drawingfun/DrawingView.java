package com.mbelkhode.drawingfun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A view where the user can start drawing. Supports the following 4 apis to help with the drawing.
 *
 *    1. setBrushMode - Sets the brush mode to enable drawing
 *    2. setEraseMode - Sets the eraser mode to enable erasing parts of the drawing
 *    3. setPainColor - Sets the paint color to the selected one
 *    4. eraseAllAndSetDefaultColor - Erases the whole canvas and sets the color to default
 *    5. saveDrawing - Saves the bitmap as an image to the gallery.
 *
 * Uses touch event to draw and also drag a brush or erase view when the user moves the finger.
 * Supports the following 3 functions to support dragging the view.
 *
 *    1. startDrag - creates the drag view on Motion Event ACTION_DOWN
 *    2. drag - drags the drag view on Motion Event ACTION_MOVE
 *    3. stopDrag - clears the drag view on Motion event ACTION_UP
 *
 */

public class DrawingView extends View {

    private final Context mContext;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private final Path mPath;
    private final Paint mBitmapPaint;
    private final Paint mPaint;
    private ImageView mDragView;
    private WindowManager mWindowManager;
    private int mLastSelectedColor;
    private int mDragResource = R.drawable.ic_paint_brush;
    private float mX, mY;

    private static final int SCALED_IMAGE_SIZE = 100;
    private static final float TOUCH_TOLERANCE = 4;
    private static final int DEFAULT_STROKE_WIDTH = 12;
    private static final int DEFAULT_ERASE_WIDTH = 20;

    private static final String SAVE_TOAST_MSG = "Saved your drawing...";

    public DrawingView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mLastSelectedColor = Color.GREEN;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * Sets the view in the brush mode.
     */
    public void setBrushMode() {
        mPaint.setColor(mLastSelectedColor);
        mDragResource = R.drawable.ic_paint_brush;
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    }

    /**
     * Sets the view in the erase mode.
     */
    public void setEraseMode() {
        mPaint.setColor(Color.WHITE);
        mDragResource = R.drawable.ic_eraser;
        mPaint.setStrokeWidth(DEFAULT_ERASE_WIDTH);
    }

    /**
     * Sets the paint color for the brush in the view
     *
     * @param color The color that the user has selected
     */
    public void setPaintColor(int color) {
        mPaint.setColor(color);
        mLastSelectedColor = color;
        mDragResource = R.drawable.ic_paint_brush;
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    }

    /**
     * Erases the canvas and sets the default color
     */
    public void eraseAllAndSetDefaultColor() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        mPaint.setColor(mLastSelectedColor);
        mDragResource = R.drawable.ic_paint_brush;
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        invalidate();
    }

    /**
     * Saves the drawing to the media gallery. The filename has a format of app name_timestamp.jpg
     */
    public void saveDrawing() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String pictureName = mContext.getString(R.string.app_name) + "_"+ timeStamp + ".jpg";
        MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                mBitmap, pictureName, timeStamp);
        Toast.makeText(mContext, SAVE_TOAST_MSG, Toast.LENGTH_SHORT).show();
    }

    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                startDrag(event);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                drag(event);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                stopDrag();
                break;
        }
        return true;
    }

    /**
     * Creates a drag view to be dragged at coordinates where the user first touches.
     *
     * @param ev The input touch event
     */
    private void startDrag(MotionEvent ev) {

        final int x = (int) ev.getRawX();
        final int y = (int) ev.getRawY();

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                mDragResource);
        bitmap = Bitmap.createScaledBitmap(bitmap, SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE, false);

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.START;
        windowParams.x = x;
        windowParams.y = y - SCALED_IMAGE_SIZE;

        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        ImageView v = new ImageView(mContext);
        v.setImageBitmap(bitmap);

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(v, windowParams);
        mDragView = v;
    }

    /**
     * Stops the drag when the user's finger goes off the screen.
     */
    private void stopDrag() {
        if (mDragView != null) {
            mDragView.setVisibility(GONE);
            mWindowManager.removeView(mDragView);
            mDragView.setImageDrawable(null);
            mDragView = null;
        }
    }

    /**
     * Drags the drag view as the user moves the finger.
     *
     * @param ev The input touch event
     */
    private void drag(MotionEvent ev) {
        final int x = (int) ev.getRawX();
        final int y = (int) ev.getRawY();
        if (mDragView != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mDragView.getLayoutParams();
            layoutParams.x = x;
            layoutParams.y = y - SCALED_IMAGE_SIZE;
            mWindowManager.updateViewLayout(mDragView, layoutParams);
        }
    }
}
