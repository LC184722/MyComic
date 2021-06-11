package com.qc.common.ui.fragment;

import com.qc.common.self.CommonData;
import com.qc.mycomic.R;

import java.util.ArrayList;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseHomeFragment;

/**
 * @author LuQiChuang
 * @desc HOME界面
 * @date 2020/8/12 15:26
 * @ver 1.0
 */
public class MyHomeFragment extends BaseHomeFragment {

    @Override
    protected boolean isExitFragment() {
        return true;
    }

    @Override
    protected boolean isNeedChangeStatusBarMode() {
        return true;
    }

    @Override
    protected boolean isViewPagerSwipe() {
        return false;
    }

    @Override
    protected boolean isDestroyItem() {
        return false;
    }

    @Override
    protected void addTabs() {
        addTab(R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_select_24, CommonData.getTabBars()[0]);
        addTab(R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_search_select_24, CommonData.getTabBars()[1]);
        addTab(R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_person_select_24, CommonData.getTabBars()[2]);
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        fragments.add(new ShelfFragment());
        fragments.add(new SearchBaseFragment());
        fragments.add(new PersonFragment());
    }

}
