package com.qc.common.ui.fragment;

import com.qc.common.constant.AppConstant;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qc.common.en.SettingEnum;
import com.qc.mycomic.ui.fragment.SearchBaseFragment;
import com.qc.mycomic.ui.fragment.ShelfFragment;
import com.qc.mynovel.ui.fragment.NSearchBaseFragment;
import com.qc.mynovel.ui.fragment.NShelfFragment;

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

    private int contentCode = (int) SettingUtil.getSettingKey(SettingEnum.READ_CONTENT);

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
        if (contentCode == AppConstant.COMIC_CODE) {
            addTab(R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_select_24, "我的画架");
            addTab(R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_search_select_24, "搜索漫画");
            addTab(R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_person_select_24, "个人中心");
        } else {
            addTab(R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_home_select_24, "我的书架");
            addTab(R.drawable.ic_baseline_search_24, R.drawable.ic_baseline_search_select_24, "搜索小说");
            addTab(R.drawable.ic_baseline_person_24, R.drawable.ic_baseline_person_select_24, "个人中心");
        }
    }

    @Override
    protected void addFragment(ArrayList<BaseFragment> fragments) {
        if (contentCode == AppConstant.COMIC_CODE) {
            fragments.add(new ShelfFragment());
            fragments.add(new SearchBaseFragment());
        } else {
            fragments.add(new NShelfFragment());
            fragments.add(new NSearchBaseFragment());
        }
        fragments.add(new PersonFragment());
    }

}
