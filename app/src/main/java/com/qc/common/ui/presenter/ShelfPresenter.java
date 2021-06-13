package com.qc.common.ui.presenter;

import com.qc.common.ui.view.ShelfView;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.EntityUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Request;
import the.one.base.ui.presenter.BasePresenter;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.self.CommonCallback;
import top.luqichuang.common.util.NetUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 18:50
 * @ver 1.0
 */
public class ShelfPresenter extends BasePresenter<ShelfView> {

    private boolean isAll = false;

    private int priority = 0;

    public void checkUpdate(List<Entity> entityList) {
        for (Entity entity : entityList) {
            if (isAll) {
                for (EntityInfo info : entity.getInfoList()) {
                    checkUpdate(entity, info);
                }
            } else {
                checkUpdate(entity, entity.getInfo());
            }
        }
    }

    private void checkUpdate(Entity entity, EntityInfo info) {
        Source source = EntityHelper.commonSource(entity);
        Request request = source.getDetailRequest(info.getDetailUrl());
        NetUtil.startLoad(request, new CommonCallback(request, source, null) {
            @Override
            public void onFailure(String errorMsg) {
                ShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        view.checkUpdateComplete(info.getTitle());
                    }
                });
            }

            @Override
            public void onResponse(String html, Map<String, Object> map) {
                ShelfView view = getView();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    if (view != null) {
                        String curUpdateChapter = info.getUpdateChapter();
                        source.setInfoDetail(info, html, map);
                        if (curUpdateChapter == null || !curUpdateChapter.equals(info.getUpdateChapter())) {
                            if (info.getUpdateChapter() != null) {
                                entity.setUpdate(true);
                                entity.setPriority(++priority);
                                EntityUtil.first(entity);
                            }
                        }
                        DBUtil.save(entity, DBUtil.SAVE_CUR);
                        view.checkUpdateComplete(null);
                    }
                });
            }
        });

    }

    public void initPriority() {
        this.priority = 0;
    }

}
