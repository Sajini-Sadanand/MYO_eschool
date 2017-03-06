package com.qualsoft.myo_eschool.utilities;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by suyati on 3/6/17.
 */

public class ProgressDialogUtility {
    static ProgressDialog pd = null;

    public static ProgressDialogUtility getInstance(Context context) {
        pd = new ProgressDialog(context);
        return new ProgressDialogUtility();
    }


    public void showProgressDialog(String message, Boolean is_cancelable) {
        pd.setMessage(message);
        pd.setCancelable(is_cancelable);
        pd.show();
    }

    public void hideProgressDialog() {
        pd.dismiss();
    }

}
