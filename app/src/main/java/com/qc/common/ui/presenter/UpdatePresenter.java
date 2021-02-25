package com.qc.common.ui.presenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.ToastUtil;
import top.luqichuang.common.jsoup.JsoupNode;
import top.luqichuang.common.util.NetUtil;

import com.qc.common.ui.view.UpdateView;
import com.qc.common.util.VersionUtil;

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
        NetUtil.startLoad(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UpdateView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.getVersionTag(null, null);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                UpdateView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        JsoupNode node = new JsoupNode(html);
                        String versionTag = node.ownText("div.tag-name span");
                        String href = node.href("div.release-tag-item div.item a");
                        view.getVersionTag(versionTag, href);
                    }
                });
            }
        });
    }

    public void checkApkUpdate() {
        NetUtil.startLoad(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                AndroidSchedulers.mainThread().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        JsoupNode node = new JsoupNode(html);
                        String versionTag = node.ownText("div.tag-name span");
                        if (existUpdate(versionTag, VersionUtil.versionName)) {
                            String title = "存在新版本" + versionTag + "，快去更新吧！";
                            ToastUtil.show(title);
                        }
                    }
                });
            }
        });
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
