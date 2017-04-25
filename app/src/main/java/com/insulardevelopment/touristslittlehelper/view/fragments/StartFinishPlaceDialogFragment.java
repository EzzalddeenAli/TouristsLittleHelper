package com.insulardevelopment.touristslittlehelper.view.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.insulardevelopment.touristslittlehelper.R;

/**
 * Created by Маргарита on 25.04.2017.
 */

public class StartFinishPlaceDialogFragment extends DialogFragment {

    private OnDialogResultListener onDialogResultListener;

    public interface OnDialogResultListener {
        public void onPositiveResult();
        public void onNegativeResult();
    }


    public void setOnDialogResultListener(OnDialogResultListener onDialogResultListener) {
        this.onDialogResultListener = onDialogResultListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.if_choose_start_and_finish_place);
        builder.setPositiveButton(R.string.ok, (dialog, id) -> onDialogResultListener.onPositiveResult());
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> onDialogResultListener.onNegativeResult());
        return builder.create();
    }
}
