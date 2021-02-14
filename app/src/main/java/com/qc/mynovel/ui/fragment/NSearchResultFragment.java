package com.qc.mynovel.ui.fragment;

import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.Constant;
import com.qc.common.util.RestartUtil;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qc.common.en.SettingEnum;
import com.qc.mynovel.ui.adapter.NSearchAdapter;
import com.qc.mynovel.ui.presenter.NSearchPresenter;
import com.qc.mynovel.ui.view.NSearchView;
import com.qc.mynovel.util.NovelHelper;
import com.qc.mynovel.util.DBUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseDataFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.QMUIDialogUtil;
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc 搜索结果界面
 * @date 2020/8/12 15:31
 * @ver 1.0
 */
public class NSearchResultFragment extends BaseDataFragment<Novel> implements NSearchView {

    private NSearchPresenter presenter = new NSearchPresenter();

    private List<Novel> novelList;

    private String searchString;

    public NSearchResultFragment() {
        RestartUtil.restart(_mActivity);
    }

    public NSearchResultFragment(String searchString) {
        this.searchString = searchString;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new NSearchAdapter(R.layout.item_search);
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
        if (novelList == null) {
            novelList = DBUtil.findNovelListByStatus(Constant.STATUS_ALL);
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        Novel novel = (Novel) adapter.getData().get(position);
        //Log.i(TAG, "onItemClick: " + novel);
        int index = novelList.indexOf(novel);
        if (index != -1) {
            Novel myNovel = novelList.get(index);
            for (NovelInfo novelInfo : novel.getNovelInfoList()) {
                if (!myNovel.getNovelInfoList().contains(novelInfo)) {
                    NovelHelper.addNovelInfo(myNovel, novelInfo);
                }
            }
            startFragment(new NChapterFragment(myNovel));
        } else {
            startFragment(new NChapterFragment(novel));
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
    private int size = NSourceUtil.getNSourceList().size();
    private List<String> errorList = new ArrayList<>();

    @Override
    public void searchComplete(List<Novel> novelList, String sourceName) {
        if (sourceName != null) {
            errorList.add(sourceName);
        }
        if (++count == size) {
//            String data = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE).getData();
//            int sourceId = Integer.parseInt(data);
            int sourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_SOURCE);
            for (Novel novel : novelList) {
                NovelHelper.changeNovelInfo(novel, sourceId);
            }
            onFirstComplete(novelList);
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
                QMUIDialogUtil.showSimpleDialog(getContext(), "搜索结果", "搜索完毕，失败小说源数：" + errorList.size() + "\n" + tip);
                errorList.clear();
            }
        } else {
            onFirstComplete(novelList);
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
