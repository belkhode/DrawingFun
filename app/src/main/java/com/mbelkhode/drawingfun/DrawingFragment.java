package com.mbelkhode.drawingfun;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment with a view where the user can start drawing. Supports the following 4 apis to help
 * with the drawing.
 *
 *    1. setBrushMode - Sets the brush mode to enable drawing
 *    2. setEraseMode - Sets the eraser mode to enable erasing parts of the drawing
 *    3. setPainColor - Sets the paint color to the selected one
 *    4. eraseAllAndSetDefaultColor - Erases the whole canvas and sets the color to default
 *    5. saveDrawing - Saves the bitmap as an image to the gallery.
 *
 */
public class DrawingFragment extends Fragment {

    private static final String LOG_TAG = DrawingFragment.class.getSimpleName();

    public DrawingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawing, container, false);
    }

    /**
     * This function sets the fragment or view in the Brush mode where a user can start drawing
     * on the canvas.
     */
    public void setBrushMode() {
        DrawingView drawingView = getDrawingView();
        if (drawingView != null) {
            drawingView.setBrushMode();
        } else {
            Log.e(LOG_TAG, "setBrushMode: Drawing view is null");
        }
    }

    /**
     * This function sets the fragment or view in the erase mode where a user can start erasing
     * the drawing on the canvas.
     */
    public void setEraseMode() {
        DrawingView drawingView = getDrawingView();
        if (drawingView != null) {
            drawingView.setEraseMode();
        } else {
            Log.e(LOG_TAG, "setEraseMode: Drawing view is null");
        }
    }

    /**
     * This function changes the color of the paint or the brush.
     * @param color The color that the user has selected
     */
    public void setPaintColor(int color) {
        DrawingView drawingView = getDrawingView();
        if (drawingView != null) {
            drawingView.setPaintColor(color);
        } else {
            Log.e(LOG_TAG, "setPaintColor: Drawing view is null");
        }
    }

    /**
     * This function will erase the whole canvas and set the color to its default mode.
     */
    public void eraseAllAndSetDefaultColor() {
        DrawingView drawingView = getDrawingView();
        if (drawingView != null) {
            drawingView.eraseAllAndSetDefaultColor();
        } else {
            Log.e(LOG_TAG, "eraseAllAndSetDefaultColor: Drawing view is null");
        }
    }

    /**
     * This function will save the drawing to the media gallery and display a toast to the user.
     */
    public void saveDrawing() {
        DrawingView drawingView = getDrawingView();
        if (drawingView != null) {
            drawingView.saveDrawing();
        } else {
            Log.e(LOG_TAG, "saveDrawing: Drawing view is null");
        }
    }

    /**
     * This function gets the drawing view from the root view.
     */
    private DrawingView getDrawingView() {
        View view = getView();
        DrawingView drawingView = null;
        if (view != null) {
            drawingView = (DrawingView) view.findViewById(R.id.DrawingView);
        } else {
            Log.e(LOG_TAG, "getDrawingView: view is null");
        }
        return drawingView;
    }
}
