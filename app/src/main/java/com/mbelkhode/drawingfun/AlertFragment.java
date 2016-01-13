package com.mbelkhode.drawingfun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

/**
 * This dialog fragment pops up the alert dialog when the user selects to erase the whole canvas.
 * The invoking activity implements the callback to receive the response back from the user. The
 * fragment accepts the title as the input of the dialog to keep the fragment generic and reusable.
 *
 */

public class AlertFragment extends DialogFragment {

    private OnResponseSelectedListener mListener;

    /**
     * Instantiates a new fragment and returns to the caller. Also saves the input parameter in the
     * bundle.
     */
    public static AlertFragment newInstance(String dialogTitle) {
        AlertFragment f = new AlertFragment();

        Bundle args = new Bundle();
        args.putString("title", dialogTitle);
        f.setArguments(args);

        return f;
    }

    /**
     * The interface to be implemented by the associated activity to get back the user response.
     */
    public interface OnResponseSelectedListener {
        void onResponseSelected(boolean isOK);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnResponseSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnResponseSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        String dialogTitle = getArguments().getString("title");
        if (dialogTitle != null) {
            alertDialogBuilder.setTitle(dialogTitle);
        }

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mListener.onResponseSelected(true);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onResponseSelected(false);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setIcon(R.drawable.ic_eraser);

        return alertDialogBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }
}
