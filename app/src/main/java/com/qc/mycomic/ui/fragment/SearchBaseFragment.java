package com.qc.mycomic.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.ui.fragment.RankFragment;
import com.qc.mycomic.ui.fragment.SearchFragment;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import top.luqichuang.mycomic.model.Source;
import top.luqichuang.common.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc 搜索基础界面
 * @date 2020/8/12 15:26
 * @ver 1.0
 */
public class SearchBaseFragment extends BaseTitleTabFragment {

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("搜索漫画");
        mTopLayout.setNeedChangedWithTheme(false);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);

        QMUIAlphaImageButton ibSearch = mTopLayout.addRightImageButton(R.drawable.ic_baseline_search_24, R.id.topbar_right_button1);
        ibSearch.setOnClickListener(v -> {
            startFragment(new SearchFragment());
        });
    }

    @Override
    protected void addTabs() {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            if (source.isValid() && source.getRankMap() != null) {
                addTab(source.getSourceName());
            }
        }
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        List<Source> sourceList = SourceUtil.getSourceList();
        for (Source source : sourceList) {
            if (source.isValid() && source.getRankMap() != null) {
                fragments.add(new RankFragment(source));
            }
        }
    }
}
