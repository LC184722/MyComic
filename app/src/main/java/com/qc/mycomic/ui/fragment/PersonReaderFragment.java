package com.qc.mycomic.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.setting.Setting;
import com.qc.mycomic.setting.SettingFactory;
import com.qc.mycomic.util.MapUtil;
import com.qc.mycomic.util.PopupUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import the.one.base.ui.fragment.BaseGroupListFragment;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/1 14:50
 * @ver 1.0
 */
public class PersonReaderFragment extends BaseGroupListFragment implements View.OnClickListener {

    private QMUICommonListItemView v1, v2, v3, v4, v5, v6;

    @Override
    protected void initView(View view) {
        super.initView(view);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("阅读配置");
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
        addTopBarBackBtn();
    }

    @Override
    protected void addGroupListView() {
        v1 = CreateDetailItemView("阅读预加载图片数量", SettingFactory.getInstance().getSetting(SettingFactory.SETTING_PRELOAD_NUM).getDetailDesc());
        v2 = CreateDetailItemView("选择画质", SettingFactory.getInstance().getSetting(SettingFactory.SETTING_COMPRESS_IMAGE).getDetailDesc());
//        addToGroup("阅读配置", v1, v2);
        addToGroup(v1, v2);
    }

    @Override
    public void onClick(View view) {
        if (view == v1) {
            Setting setting = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_PRELOAD_NUM);
            PopupUtil.showSimpleBottomSheetList(getContext(), setting.getMyMap(), "选择预加载图片数量", setting.getData(), new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    setting.setData(MapUtil.getKeyByValue(setting.getMyMap(), tag));
                    v2.setDetailText(tag);
                    dialog.dismiss();
                }
            });
        } else if (view == v2) {
            Setting setting = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_COMPRESS_IMAGE);
            PopupUtil.showSimpleBottomSheetList(getContext(), setting.getMyMap(), "选择画质（如发生卡顿、闪退请选择低画质）", setting.getData(), new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    setting.setData(MapUtil.getKeyByValue(setting.getMyMap(), tag));
                    v5.setDetailText(tag);
                    dialog.dismiss();
                }
            });
        }
    }
}