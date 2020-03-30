package com.lgc.baselibrary.UIWidgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class CertainDialog {
    Context mContext;
    AlertDialog dialog;

    public interface ActionListener {
        void onSure();
    }

    public CertainDialog(Context context) {
        this.mContext = context;
    }

    public void showDialog(String title, String msg, final ActionListener actionListener) {

        if (dialog != null && dialog.isShowing()) return;
        dialog = new AlertDialog.Builder(mContext)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        actionListener.onSure();
                    }
                })
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .create();
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

}
