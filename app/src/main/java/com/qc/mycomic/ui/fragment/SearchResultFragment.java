package com.qc.mycomic.ui.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.R;
import com.qc.mycomic.setting.Setting;
import com.qc.mycomic.ui.adapter.SearchAdapter;
import com.qc.mycomic.model.Comic;
import com.qc.mycomic.model.ComicInfo;
import com.qc.mycomic.ui.presenter.SearchPresenter;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qc.mycomic.ui.view.SearchView;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.LinkedList;
import java.util.List;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.util.SpUtil;

/**
 * @author LuQiChuang
 * @description 搜索结果界面
 * @date 2020/8/12 15:31
 * @ver 1.0
 */
public class SearchResultFragment extends BaseDataFragment<Comic> implements SearchView {

    private SearchPresenter presenter = new SearchPresenter();

    private List<Comic> comicList;

    private String searchString;

    public SearchResultFragment(String searchString) {
        this.searchString = searchString;
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
        if (comicList == null) {
            comicList = DBUtil.findComicListByStatus(Codes.STATUS_ALL);
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Comic comic = (Comic) adapter.getData().get(position);
        Log.i(TAG, "onItemClick: " + comic);
        int index = comicList.indexOf(comic);
        if (index != -1) {
            Comic myComic = comicList.get(index);
            for (ComicInfo comicInfo : comic.getComicInfoList()) {
                if (!myComic.getComicInfoList().contains(comicInfo)) {
                    myComic.addComicInfo(comicInfo);
                }
            }
            startFragment(new ChapterFragment(myComic));
        } else {
            startFragment(new ChapterFragment(comic));
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
    private int size = SourceUtil.getSourceList().size();
    private List<String> errorList = new LinkedList<>();

    @Override
    public void searchComplete(List<Comic> comicList, String sourceName) {
        if (sourceName != null) {
            errorList.add(sourceName);
        }
        if (++count == size) {
            int sourceId = Setting.getDefaultSourceId();
            for (Comic comic : comicList) {
                comic.changeComicInfo(sourceId);
            }
            onFirstComplete(comicList);
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
                QMUIDialogUtil.showSimpleDialog(getContext(), "搜索结果", "搜索完毕，失败漫画源数：" + errorList.size() + "\n" + tip);
                errorList.clear();
            }
        } else {
            onFirstComplete(comicList);
            adapter.notifyDataSetChanged();
            showProgressDialog(getPercent(), total);
            progressDialog.setMessage(getMsg());
        }
        Log.i(TAG, "searchComplete: " + getLoadProcess());
    }

    private int getPercent() {
        if (size != 0) {
            return count * total / size;
        } else {
            return 100;
        }
    }

    private String getLoadProcess() {
        return count + "/" + size;
    }

    private String getMsg() {
        return "正在搜索 " + getLoadProcess();
    }
}
