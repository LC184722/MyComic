package com.qc.mycomic.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.setting.Setting;
import com.qc.mycomic.setting.SettingFactory;
import com.qc.mycomic.ui.presenter.UpdatePresenter;
import com.qc.mycomic.ui.view.UpdateView;
import com.qc.mycomic.util.Codes;
import com.qc.mycomic.util.DBUtil;
import com.qc.mycomic.util.PackageUtil;
import com.qc.mycomic.util.PopupUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
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

    private QMUICommonListItemView web, version, v1, v2, v3, v4;

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

//    private int defaultSourceId = Setting.getDefaultSourceId();

    @Override
    protected void addGroupListView() {
        web = CreateNormalItemView("访问主页");
        version = CreateDetailItemView("检查更新", PackageUtil.getVersionName(_mActivity));
        v1 = CreateDetailItemView("默认漫画源", SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE).getDetailDesc());
        v2 = CreateDetailItemView("阅读预加载图片数量", SettingFactory.getInstance().getSetting(SettingFactory.SETTING_PRELOAD_NUM).getDetailDesc());
        v3 = CreateDetailItemView("备份数据");
        v4 = CreateDetailItemView("还原数据");
        addToGroup("设置", v1, v2);
        addToGroup("数据", v3, v4);
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
            Setting setting = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE);
            PopupUtil.showSimpleBottomSheetList(getContext(), setting.getMyMap(), "选择默认漫画源", setting.getData(), new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    setting.setData(setting.getMyMap().getKeyByValue(tag));
                    v1.setDetailText(tag);
                    dialog.dismiss();
                }
            });
        } else if (view == v2) {
            Setting setting = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_PRELOAD_NUM);
            PopupUtil.showSimpleBottomSheetList(getContext(), setting.getMyMap(), "选择预加载图片数量", setting.getData(), new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    setting.setData(setting.getMyMap().getKeyByValue(tag));
                    v2.setDetailText(tag);
                    dialog.dismiss();
                }
            });
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
        }
    }

    @Override
    public void getVersionTag(String versionTag) {
        hideLoadingDialog();
        if (existUpdate(versionTag, Codes.versionTag)) {
            showFailTips("存在新版本" + versionTag + "，请访问主页更新");
        } else {
            showSuccessTips("已是最新版本");
        }
    }

    public boolean existUpdate(String updateTag, String localTag) {
        boolean flag = false;
        if (updateTag != null && localTag != null) {
            if (updateTag.equals(localTag)) {
                flag = true;
            } else {
                String[] tags = updateTag.replace("v", "").split("\\.");
                String[] locals = localTag.replace("v", "").split("\\.");
                try {
                    for (int i = 0; i < tags.length; i++) {
                        int tag = Integer.parseInt(tags[i]);
                        int local = Integer.parseInt(locals[i]);
                        if (tag > local) {
                            flag = true;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    @Override
    public UpdatePresenter getPresenter() {
        return presenter;
    }
}
