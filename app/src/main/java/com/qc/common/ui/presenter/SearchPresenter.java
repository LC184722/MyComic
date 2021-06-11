package com.qc.common.ui.presenter;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.view.SearchView;
import com.qc.common.util.EntityHelper;

import java.util.ArrayList;
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
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 15:33
 * @ver 1.0
 */
public class SearchPresenter extends BasePresenter<SearchView> {

    private List<Entity> entityList;

    public void search(String searchString) {
        entityList = new ArrayList<>();
        List sourceList;
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            sourceList = SourceUtil.getSourceList();
        } else {
            sourceList = SourceUtil.getNSourceList();
        }
        for (Object o : sourceList) {
            Source<EntityInfo> source = (Source<EntityInfo>) o;
            Request request = source.getSearchRequest(searchString);
            NetUtil.startLoad(request, new CommonCallback(request, source, Source.SEARCH) {
                @Override
                public void onFailure(String errorMsg) {
                    SearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            view.searchComplete(entityList, source.getSourceName());
                        }
                    });
                }

                @Override
                public void onResponse(String html, Map<String, Object> map) {
                    SearchView view = getView();
                    AndroidSchedulers.mainThread().scheduleDirect(() -> {
                        if (view != null) {
                            List<EntityInfo> infoList = source.getInfoList(html);
                            mergeInfoList(infoList);
                            view.searchComplete(entityList, null);
                        }
                    });
                }
            });
        }
    }

    private void mergeInfoList(List<EntityInfo> infoList) {
        for (EntityInfo entityInfo : infoList) {
            boolean isExists = false;
            for (Entity entity : entityList) {
                if (entity.getTitle().equals(entityInfo.getTitle())) {
                    isExists = true;
                    EntityHelper.addInfo(entity, entityInfo);
                }
            }
            if (!isExists) {
                Entity entity;
                if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                    entity = new Comic((ComicInfo) entityInfo);
                } else {
                    entity = new Novel((NovelInfo) entityInfo);
                }
                entityList.add(entity);
            }
        }
    }

}
