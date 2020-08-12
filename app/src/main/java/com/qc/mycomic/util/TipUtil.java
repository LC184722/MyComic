package com.qc.mycomic.util;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * @author LuQiChuang
 * @description
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class TipUtil {

    public static void showTip(View view, String text) {
        QMUITipDialog tipDialog = new QMUITipDialog.Builder(view.getContext())
                .setTipWord(text)
                .create();
        tipDialog.show();
        view.postDelayed(tipDialog::dismiss, 1000);
    }

}
