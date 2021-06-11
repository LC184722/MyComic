package com.qc.common.ui.presenter;

import com.qc.common.ui.view.ReaderView;
import com.qc.common.util.EntityHelper;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.ChapterInfo;
import top.luqichuang.common.model.Content;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.self.CommonCallback;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 22:30
 * @ver 1.0
 */
public class ReaderPresenter extends BasePresenter<ReaderView> {

    public void loadContentInfoList(Entity entity) {
        List<ChapterInfo> chapterInfoList = entity.getInfo().getChapterInfoList();
        int position = EntityHelper.getPosition(entity.getInfo());
        int chapterId = entity.getInfo().getCurChapterId();
        String url = chapterInfoList.get(position).getChapterUrl();
        Source source = EntityHelper.commonSource(entity);
        Request request = source.getContentRequest(url);
        NetUtil.startLoad(request, new CommonCallback(request, source, Source.CONTENT) {
            @Override
            public void onFailure(String errorMsg) {
                ReaderView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.loadReadContentComplete(null, errorMsg);
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                ReaderView view = getView();
                List<Content> list = EntityHelper.getContentList(entity, html, chapterId, map);
                if (list.isEmpty()) {
                    onFailure("解析失败！");
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.loadReadContentComplete(list, null);
                        }
                    });
                }
            }
        });
    }

}
