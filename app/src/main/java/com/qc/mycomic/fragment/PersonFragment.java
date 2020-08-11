package com.qc.mycomic.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.util.PackageUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import the.one.base.Interface.IApkUpdate;
import the.one.base.ui.activity.BaseWebExplorerActivity;
import the.one.base.ui.fragment.BaseFragment;
import the.one.base.ui.fragment.BaseGroupListFragment;
import the.one.base.ui.presenter.BasePresenter;
import the.one.base.util.BaseUpdateApkUtil;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.widge.RoundImageView;

public class PersonFragment extends BaseGroupListFragment implements View.OnClickListener {

    private QMUICommonListItemView v1, v2, v3;

    @Override
    protected boolean isNeedChangeStatusBarMode() {
        return true;
    }

    @Override
    protected boolean isStatusBarLightMode() {
        return true;
    }

    @Override
    protected boolean translucentFull() {
        return true;
    }

    @Override
    protected Object getTopLayout() {
        return R.layout.top_person;
    }

    @Override
    protected int getScrollViewParentBgColor() {
        return R.color.qmui_config_color_white;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("个人中心");
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
        RoundImageView imageView = findViewByTopView(R.id.imageView);
        imageView.setImageDrawable(getDrawablee(R.drawable.head));
    }

    @Override
    protected void addGroupListView() {
        v1 = CreateNormalItemView("访问网站");
        v2 = CreateDetailItemView("检查更新", PackageUtil.getVersionName(_mActivity));
        addToGroup("关于", v1, v2);
    }

    @Override
    public void onClick(View view) {
        String url = "https://gitee.com/luqichuang/MyComic";
        String title = "MyComic";
        if (view == v1) {
            BaseWebExplorerActivity.newInstance(_mActivity, title, url);
        } else if (view == v2) {



            QMUIDialogUtil.SuccessTipsDialog(getContext(), PackageUtil.getVersionName(_mActivity));
        }
    }
}
