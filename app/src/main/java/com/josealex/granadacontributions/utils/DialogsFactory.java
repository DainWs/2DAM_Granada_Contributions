package com.josealex.granadacontributions.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.josealex.granadacontributions.R;

public class DialogsFactory {

    public static void makeAreYouSureDialog(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GlobalInformation.mainActivity)
                .setTitle(R.string.warnning)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(
                        R.string.delete_button_text,
                        okListener
                )
                .setNegativeButton(
                        R.string.cancel_button_text,
                        (dialogD, id) -> dialogD.dismiss()
                )
                .create()
                .show();
    }

    public static void makeAddToShoppingCartDialog(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GlobalInformation.mainActivity)
                .setTitle(R.string.shopping_cart)
                .setMessage(R.string.add_to_shopping_cart)
                .setPositiveButton(
                        R.string.add,
                        okListener
                )
                .setNegativeButton(
                        R.string.cancel_button_text,
                        (dialogD, id) -> dialogD.dismiss()
                )
                .create()
                .show();
    }
}
