package com.qc.mycomic.ui.activity;

import com.qc.mycomic.ui.fragment.MyHomeFragment;
import com.qc.mycomic.ui.presenter.UpdatePresenter;

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

    private UpdatePresenter presenter = new UpdatePresenter();

    @Override
    protected BaseFragment getFirstFragment() {
        LitePal.initialize(this);
        presenter.checkApkUpdate();
        return new MyHomeFragment();
    }

}