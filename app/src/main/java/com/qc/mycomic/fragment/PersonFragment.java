package com.qc.mycomic.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;

import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.presenter.BasePresenter;

public class PersonFragment extends BaseFragment {

    private QMUIQQFaceView mTitle;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initView(View rootView) {
        mTitle = mTopLayout.setTitle("个人中心");
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
