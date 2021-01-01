package com.qc.mycomic.ui.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.util.ToastUtil;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.ui.view.UpdateView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.IOException;


/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class UpdatePresenter extends BasePresenter<UpdateView> {

    private String url = "https://gitee.com/luqichuang/MyComic/releases";

    public void checkUpdate() {
        UpdateView view = getView();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.getVersionTag(null);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                JsoupNode node = new JsoupNode(html);
                String versionTag = node.ownText("div.tag-name span");
                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.getVersionTag(versionTag);
                        }
                    }
                });
            }
        };
        NetUtil.startLoad(url, callback);
    }

    public void checkApkUpdate() {
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                JsoupNode node = new JsoupNode(html);
                String versionTag = node.ownText("div.tag-name span");

                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        if (existUpdate(versionTag, Codes.versionTag)) {
                            String title = "存在新版本" + versionTag + "，快去更新吧！";
                            ToastUtil.show(title);
                        }
                    }
                });
            }
        };
        NetUtil.startLoad(url, callback);
    }

    public boolean existUpdate(String updateTag, String localTag) {
        boolean flag = false;
        if (updateTag != null && localTag != null && !updateTag.equals(localTag)) {
            String[] tags = updateTag.replace("v", "").split("\\.");
            String[] locals = localTag.replace("v", "").split("\\.");
            try {
                for (int i = 0; i < tags.length; i++) {
                    int tag = Integer.parseInt(tags[i]);
                    int local = Integer.parseInt(locals[i]);
                    if (tag > local) {
                        flag = true;
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

}
