package com.qc.mycomic.ui.fragment;

import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.mycomic.R;
import com.qc.mycomic.constant.Constant;
import com.qc.mycomic.en.SettingEnum;
import com.qc.mycomic.ui.adapter.SearchAdapter;
import com.qc.mycomic.ui.presenter.SearchPresenter;
import com.qc.mycomic.ui.view.SearchView;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.RestartUtil;
import com.qc.mycomic.util.SettingUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import top.luqichuang.common.mycomic.model.Comic;
import top.luqichuang.common.mycomic.model.ComicInfo;
import com.qc.mycomic.util.ComicHelper;
import top.luqichuang.common.mycomic.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc 搜索结果界面
 * @date 2020/8/12 15:31
 * @ver 1.0
 */
public class SearchResultFragment extends BaseDataFragment<Comic> implements SearchView {

    private SearchPresenter presenter = new SearchPresenter();

    private List<Comic> comicList;

    private String searchString;

    public SearchResultFragment() {
        RestartUtil.restart(_mActivity);
    }

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
            comicList = DBUtil.findComicListByStatus(Constant.STATUS_ALL);
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Comic comic = (Comic) adapter.getData().get(position);
        //Log.i(TAG, "onItemClick: " + comic);
        int index = comicList.indexOf(comic);
        if (index != -1) {
            Comic myComic = comicList.get(index);
            for (ComicInfo comicInfo : comic.getComicInfoList()) {
                if (!myComic.getComicInfoList().contains(comicInfo)) {
                    ComicHelper.addComicInfo(myComic, comicInfo);
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
    private List<String> errorList = new ArrayList<>();

    @Override
    public void searchComplete(List<Comic> comicList, String sourceName) {
        if (sourceName != null) {
            errorList.add(sourceName);
        }
        if (++count == size) {
//            String data = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE).getData();
//            int sourceId = Integer.parseInt(data);
            int sourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_SOURCE);
            for (Comic comic : comicList) {
                ComicHelper.changeComicInfo(comic, sourceId);
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
        //Log.i(TAG, "searchComplete: " + getLoadProcess());
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
