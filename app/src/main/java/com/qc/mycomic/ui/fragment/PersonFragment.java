package com.qc.mycomic.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.ui.presenter.UpdatePresenter;
import com.qc.mycomic.ui.view.UpdateView;
import com.qc.mycomic.constant.Constant;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.VersionUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import the.one.base.ui.fragment.BaseGroupListFragment;
import the.one.base.util.QMUIDialogUtil;
import the.one.base.widge.RoundImageView;

/**
 * @author LuQiChuang
 * @desc 个人中心界面
 * @date 2020/8/12 15:19
 * @ver 1.0
 */
public class PersonFragment extends BaseGroupListFragment implements View.OnClickListener, UpdateView {

    private QMUICommonListItemView web, version, v1, v2, v3, v4, v5;

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
        web = CreateNormalItemView("访问主页");
        version = CreateDetailItemView("检查更新", VersionUtil.getVersionName(_mActivity));
        v1 = CreateDetailItemView("漫画源配置", "", true);
        v2 = CreateDetailItemView("阅读配置", "", true);
        v3 = CreateDetailItemView("备份数据");
        v4 = CreateDetailItemView("还原数据");
        v5 = CreateDetailItemView("留言反馈", "提出您宝贵的建议", true);
        addToGroup("设置", v1, v2);
        addToGroup("数据", v3, v4);
        addToGroup("关于", web, v5, version);
    }

    @Override
    public void onClick(View view) {
        if (view == web) {
            String url = "https://gitee.com/luqichuang/MyComic";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view == version) {
            showLoadingDialog("正在检查更新");
            presenter.checkUpdate();
        } else if (view == v1) {
            startFragment(new PersonSourceFragment());
        } else if (view == v2) {
            startFragment(new PersonReaderFragment());
        } else if (view == v3) {
            QMUIDialogUtil.showSimpleDialog(getContext(), "备份漫画", "是否备份漫画数据？", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    showLoadingDialog("正在备份");
                    boolean flag = DBUtil.backupData(_mActivity);
                    hideLoadingDialog();
                    if (flag) {
                        showSuccessTips("备份成功");
                    } else {
                        showFailTips("备份失败");
                    }
                    dialog.dismiss();
                }
            }).show();
        } else if (view == v4) {
            QMUIDialogUtil.showSimpleDialog(getContext(), "还原漫画", "是否还原漫画数据？", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    showLoadingDialog("正在还原");
                    boolean flag = DBUtil.restoreData(_mActivity);
                    hideLoadingDialog();
                    if (flag) {
                        showSuccessTips("还原成功");
                    } else {
                        showFailTips("还原失败");
                    }
                    dialog.dismiss();
                }
            }).show();
        } else if (view == v5) {
            String url = "https://gitee.com/luqichuang/MyComic/issues/new";
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void getVersionTag(String versionTag) {
        hideLoadingDialog();
        if (presenter.existUpdate(versionTag, VersionUtil.versionName)) {
            String title = "存在新版本" + versionTag;
            String content = "是否前往更新页面？";
            QMUIDialogUtil.showSimpleDialog(getContext(), title, content, new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    String url = "https://gitee.com/luqichuang/MyComic/releases";
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }).showWithImmersiveCheck();
        } else {
            showSuccessTips("已是最新版本");
        }
    }

    @Override
    public UpdatePresenter getPresenter() {
        return presenter;
    }
}
