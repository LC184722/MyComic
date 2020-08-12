package com.qc.mycomic.ui.fragment;

import com.qc.mycomic.R;

import java.util.ArrayList;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseHomeFragment;

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
    protected void addTabs() {
        addTab(R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_select_24, "我的画架");
        addTab(R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_search_select_24, "搜索漫画");
        addTab(R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_person_select_24, "个人中心");
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        fragments.add(new ShelfFragment());
        fragments.add(new SearchBaseFragment());
        fragments.add(new PersonFragment());
    }

}
