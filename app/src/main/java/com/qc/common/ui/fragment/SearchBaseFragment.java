package com.qc.common.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.TmpData;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 11:23
 * @ver 1.0
 */
public class SearchBaseFragment extends BaseTitleTabFragment {

    private List<Source> sourceList;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("排行榜");
        mTopLayout.setNeedChangedWithTheme(false);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);

        QMUIAlphaImageButton ibSearch = mTopLayout.addRightImageButton(R.drawable.ic_baseline_search_24, R.id.topbar_right_button1);
        ibSearch.setOnClickListener(v -> {
            startFragment(new SearchFragment());
        });
        initList();
    }

    private void initList() {
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            sourceList = (List) SourceUtil.getSourceList();
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            sourceList = (List) SourceUtil.getNSourceList();
        } else {
            sourceList = (List) SourceUtil.getVSourceList();
        }
    }

    @Override
    protected void addTabs() {
        for (Source source : sourceList) {
            if (source.isValid() && source.getRankMap() != null) {
                addTab(source.getSourceName());
            }
        }
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        for (Source source : sourceList) {
            if (source.isValid() && source.getRankMap() != null) {
                fragments.add(RankFragment.getInstance(source.getSourceId()));
            }
        }
    }
}