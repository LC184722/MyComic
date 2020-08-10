package com.qc.mycomic.util;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class TipUtil {

    public static void showTip(View view, String text) {
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(view.getContext())
                .setTipWord(text)
                .create();
        tipDialog.show();
        view.postDelayed(tipDialog::dismiss, 1000);
    }

}
