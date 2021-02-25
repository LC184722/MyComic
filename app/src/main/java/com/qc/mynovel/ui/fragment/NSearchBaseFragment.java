package com.qc.mynovel.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseTitleTabFragment;
import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.mynovel.model.NSource;

/**
 * @author LuQiChuang
 * @desc 搜索基础界面
 * @date 2020/8/12 15:26
 * @ver 1.0
 */
public class NSearchBaseFragment extends BaseTitleTabFragment {

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("搜索小说");
        mTopLayout.setNeedChangedWithTheme(false);
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);

        QMUIAlphaImageButton ibSearch = mTopLayout.addRightImageButton(R.drawable.ic_baseline_search_24, R.id.topbar_right_button1);
        ibSearch.setOnClickListener(v -> {
            startFragment(new NSearchFragment());
        });
    }

    @Override
    protected void addTabs() {
        List<NSource> nSourceList = NSourceUtil.getNSourceList();
        for (NSource nSource : nSourceList) {
            if (nSource.isValid() && nSource.getRankMap() != null) {
                addTab(nSource.getNSourceName());
            }
        }
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        List<NSource> nSourceList = NSourceUtil.getNSourceList();
        for (NSource nSource : nSourceList) {
            if (nSource.isValid() && nSource.getRankMap() != null) {
                fragments.add(NRankFragment.getInstance(nSource.getNSourceId()));
            }
        }
    }
}