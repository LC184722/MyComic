package com.qc.mycomic.ui.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.ui.presenter.UpdatePresenter;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.PackageUtil;
import com.qc.mycomic.ui.view.UpdateView;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import the.one.base.ui.activity.BaseWebExplorerActivity;
import the.one.base.ui.fragment.BaseGroupListFragment;
import the.one.base.widge.RoundImageView;

/**
 * @author LuQiChuang
 * @description 个人中心界面
 * @date 2020/8/12 15:19
 * @ver 1.0
 */
public class PersonFragment extends BaseGroupListFragment implements View.OnClickListener, UpdateView {

    private QMUICommonListItemView v1, v2, v3;

    private UpdatePresenter presenter = new UpdatePresenter();

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
        v1 = CreateNormalItemView("访问主页");
        v2 = CreateDetailItemView("检查更新", PackageUtil.getVersionName(_mActivity));
        addToGroup("关于", v1, v2);
    }

    @Override
    public void onClick(View view) {
        String url = "https://gitee.com/luqichuang/MyComic/releases";
        String title = "MyComic";
        if (view == v1) {
            BaseWebExplorerActivity.newInstance(_mActivity, title, url);
        } else if (view == v2) {
            showLoadingDialog("正在检查更新");
            presenter.checkUpdate();
        }
    }

    @Override
    public void getVersionTag(String versionTag) {
        hideLoadingDialog();
        if (versionTag != null && !versionTag.equals(Codes.versionTag)) {
            showFailTips("存在新版本" + versionTag + "，请访问主页更新");
        } else {
            showSuccessTips("已是最新版本");
        }
    }

    @Override
    public UpdatePresenter getPresenter() {
        return presenter;
    }
}
