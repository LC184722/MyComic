package com.qc.mycomic.presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.view.UpdateView;

import java.io.IOException;


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
                            view.checkApkUpdate(false);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                JsoupNode node = new JsoupNode(html);
                String tagName = removeSuffix(node.ownText("div.tag-name span"));
                String versionTag = removeSuffix(Codes.versionTag);
                boolean isUpdate = !tagName.equals(versionTag);
                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) {
                            view.checkApkUpdate(isUpdate);
                        }
                    }
                });
            }
        };
        NetUtil.startLoad(url, callback);
    }

    private String removeSuffix(String string) {
        if (string == null) {
            return "";
        }
        while (string.endsWith(".0")) {
            string = string.substring(0, string.lastIndexOf('.'));
        }
        return string;
    }

}
