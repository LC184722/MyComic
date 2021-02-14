package com.qc.common.ui.activity;

import com.qc.common.ui.fragment.MyHomeFragment;

import the.one.base.ui.activity.BaseFragmentActivity;
import the.one.base.ui.fragment.BaseFragment;

/**
 * @author LuQiChuang
 * @desc
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class MainActivity extends BaseFragmentActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return new MyHomeFragment();
    }

}