package com.qc.mycomic.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.setting.Setting;
import com.qc.mycomic.ui.presenter.UpdatePresenter;
import com.qc.mycomic.ui.view.UpdateView;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.PackageUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.List;

import the.one.base.model.PopupItem;
import the.one.base.ui.fragment.BaseGroupListFragment;
import the.one.base.util.QMUIBottomSheetUtil;
import the.one.base.widge.RoundImageView;

/**
 * @author LuQiChuang
 * @description 个人中心界面
 * @date 2020/8/12 15:19
 * @ver 1.0
 */
public class PersonFragment extends BaseGroupListFragment implements View.OnClickListener, UpdateView {

    private QMUICommonListItemView web, version, v1, v2;

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

    private int defaultSourceId = Setting.getDefaultSourceId();

    @Override
    protected void addGroupListView() {
        web = CreateNormalItemView("访问主页");
        version = CreateDetailItemView("检查更新", PackageUtil.getVersionName(_mActivity));
        v1 = CreateDetailItemView("默认漫画源", SourceUtil.getSourceName(defaultSourceId));
        v2 = CreateDetailItemView("阅读预加载图片数量", Setting.getPreloadNumTag());
        addToGroup("设置", v1, v2);
        addToGroup("关于", web, version);
    }

    @Override
    public void onClick(View view) {
        String url = "https://gitee.com/luqichuang/MyComic/releases";
        if (view == web) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (view == version) {
            showLoadingDialog("正在检查更新");
            presenter.checkUpdate();
        } else if (view == v1) {
            List<PopupItem> list = SourceUtil.getPopupItemList();
            int index = SourceUtil.getPopupItemIndex(defaultSourceId);
            QMUIBottomSheetUtil.showSimpleBottomSheetList(getContext(), list, "选择默认漫画源", index, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    int sourceId = SourceUtil.getSourceId(tag);
                    Setting.setDefaultSourceId(sourceId);
                    v1.setDetailText(tag);
                    dialog.dismiss();
                }
            }).show();
        } else if (view == v2) {
            List<PopupItem> list = Setting.getPreloadNumItemList();
            int index = Setting.getPreloadNumIndex();
            QMUIBottomSheetUtil.showSimpleBottomSheetList(getContext(), list, "选择阅读预加载图片数量", index, new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    int preloadNum = Setting.getPreloadNumByTag(tag);
                    Setting.setPreloadNum(preloadNum);
                    v2.setDetailText(tag);
                    dialog.dismiss();
                }
            }).show();
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
