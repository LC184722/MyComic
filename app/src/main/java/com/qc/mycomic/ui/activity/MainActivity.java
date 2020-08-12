package com.qc.mycomic.ui.activity;

import com.qc.mycomic.ui.fragment.MyHomeFragment;

import org.litepal.LitePal;

import the.one.base.ui.activity.BaseFragmentActivity;
import the.one.base.ui.fragment.BaseFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected BaseFragment getFirstFragment() {
//        Utils.verifyStoragePermissions(this);
        LitePal.initialize(this);
        return new MyHomeFragment();
    }

}