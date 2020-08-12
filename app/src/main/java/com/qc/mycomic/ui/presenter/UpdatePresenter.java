package com.qc.mycomic.ui.presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;

import com.qc.mycomic.jsoup.JsoupNode;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.NetUtil;
import com.qc.mycomic.ui.view.UpdateView;

import java.io.IOException;


/**
 * @author LuQiChuang
 * @description
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

}
