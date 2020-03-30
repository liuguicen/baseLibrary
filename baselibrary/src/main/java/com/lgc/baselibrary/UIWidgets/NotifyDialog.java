package com.lgc.baselibrary.UIWidgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;

/**
 * <pre>
 *      author : liuguicen
 *      time : 2018/12/31
 *      version : 1.0
 * <pre>
 */

public class NotifyDialog {
    Context mContext;
    AlertDialog dialog;

    public interface ActionListener {
        void onSure();
    }

    public NotifyDialog(Context context) {
        this.mContext = context;
    }

    public void showDialog(String title, String msg, final CertainDialog.ActionListener actionListener) {

        if (dialog != null && dialog.isShowing()) return;
        dialog = new AlertDialog.Builder(mContext)
                .setMessage(msg)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (actionListener != null)
                            actionListener.onSure();
                    }
                })
                .setCancelable(false)
                .create();
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
