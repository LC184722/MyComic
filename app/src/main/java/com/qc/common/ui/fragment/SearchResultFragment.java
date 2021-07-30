package com.qc.common.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;
import com.qc.common.ui.adapter.SearchAdapter;
import com.qc.common.ui.presenter.SearchPresenter;
import com.qc.common.ui.view.SearchView;
import com.qc.common.util.DBUtil;
import com.qc.common.util.EntityHelper;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 15:32
 * @ver 1.0
 */
public class SearchResultFragment extends BaseDataFragment<Entity> implements SearchView {

    private SearchPresenter presenter = new SearchPresenter();

    private List<Entity> entityList;

    private String searchString;

    public static SearchResultFragment getInstance(String searchString) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchString", searchString);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.searchString = (String) getArguments().get("searchString");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new SearchAdapter(R.layout.item_search);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("搜索结果:" + searchString);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
        addTopBarBackBtn();
    }

    @Override
    protected void requestServer() {
        count = 0;
        presenter.search(searchString);
        showContentPage();
        if (progressDialog == null) {
            showProgressDialog(getPercent(), total, getMsg());
        } else {
            progressDialog.setProgress(getPercent(), total);
            progressDialog.setMessage(getMsg());
            progressDialog.show();
        }
        if (entityList == null) {
            entityList = (List<Entity>) DBUtil.findListByStatus(Constant.STATUS_ALL);
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Entity entity = (Entity) adapter.getData().get(position);
        //Log.i(TAG, "onItemClick: " + entity);
        int index = entityList.indexOf(entity);
        if (index != -1) {
            Entity myEntity = entityList.get(index);
            for (EntityInfo entityInfo : entity.getInfoList()) {
                if (!myEntity.getInfoList().contains(entityInfo)) {
                    EntityHelper.addInfo(myEntity, entityInfo);
                }
            }
            TmpData.toStatus = Constant.SEARCH_TO_CHAPTER;
            startFragment(ChapterFragment.getInstance(myEntity));
        } else {
            TmpData.toStatus = Constant.SEARCH_TO_CHAPTER;
            startFragment(ChapterFragment.getInstance(entity));
        }
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        return false;
    }

    @Override
    protected void initAdapter() {
        super.initAdapter();
        adapter.getLoadMoreModule().setOnLoadMoreListener(null);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    private int count = 0;
    private int total = 100;
    private int size = 0;
    private List<String> errorList = new ArrayList<>();

    @Override
    public void searchComplete(List<Entity> entityList, String sourceName) {
        if (sourceName != null) {
            errorList.add(sourceName);
        }
        if (++count == size) {
            int sourceId;
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                sourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_SOURCE);
            } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                sourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_NOVEL_SOURCE);
            } else {
                sourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_VIDEO_SOURCE);
            }
            for (Entity entity : entityList) {
                EntityHelper.changeInfo(entity, sourceId);
            }
            onFirstComplete(entityList);
            adapter.notifyDataSetChanged();
            count = 0;
            hideProgressDialog();
            if (errorList.isEmpty()) {
                showSuccessTips("搜索完毕");
            } else {
                StringBuilder tip = new StringBuilder();
                for (String s : errorList) {
                    tip.append(s).append("\n");
                }
                QMUIDialogUtil.showSimpleDialog(getContext(), "搜索结果", "搜索完毕，失败" + TmpData.content + "源数：" + errorList.size() + "\n" + tip);
                errorList.clear();
            }
        } else {
            onFirstComplete(entityList);
            adapter.notifyDataSetChanged();
            showProgressDialog(getPercent(), total);
            progressDialog.setMessage(getMsg());
        }
    }

    private int getPercent() {
        checkSize();
        if (size != 0) {
            return count * total / size;
        } else {
            return 100;
        }
    }

    private String getLoadProcess() {
        checkSize();
        return count + "/" + size;
    }

    private String getMsg() {
        return "正在搜索 " + getLoadProcess();
    }

    private void checkSize() {
        if (size == 0) {
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                size = SourceUtil.size();
            } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                size = SourceUtil.nSize();
            } else {
                size = SourceUtil.vSize();
            }
        }
    }
}
