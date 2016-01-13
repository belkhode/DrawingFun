package com.mbelkhode.drawingfun;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

/**
 * This dialog fragment pops up the predefined colors palette and lets the user choose the color.
 * The invoking activity implements the callback onColorSelected to get back the color chosen
 * by the user.
 */

public class ColorSelectionFragment extends DialogFragment implements View.OnClickListener {

    private OnColorSelectedListener mListener;

    /**
     * The interface to be implemented by the associated activity to get back the selected color.
     */
    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    /**
     * Instantiates a new fragment and returns to the caller.
     */
    public static ColorSelectionFragment newInstance() {
        return new ColorSelectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_color_selection, container, false);
        ImageButton button = (ImageButton) v.findViewById(R.id.brown);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.red);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.orange);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.yellow);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.green);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.darkgreen);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.blue);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.magenta);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.pink);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.white);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.grey);
        button.setOnClickListener(this);
        button = (ImageButton) v.findViewById(R.id.black);
        button.setOnClickListener(this);

        getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
        return v;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnColorSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnColorSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setTitle(R.string.select_color);
        getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_paint_color);
    }

    /**
     * Called when the user clicks on or selects a color.
     */
    @Override
    public void onClick(@NonNull View view) {
        //Dismiss the dialog
        getDialog().dismiss();

        //Get the color and convert it to an integer to be returned back
        Object color = view.getTag();
        int col = Color.parseColor((String)color);
        mListener.onColorSelected(col);
    }
}
