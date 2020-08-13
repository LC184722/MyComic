package com.qc.mycomic.ui.activity;

import android.util.Log;

import com.qc.mycomic.ui.fragment.MyHomeFragment;

import org.litepal.LitePal;

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
//        Utils.verifyStoragePermissions(this);
        LitePal.initialize(this);
        return new MyHomeFragment();
    }

}