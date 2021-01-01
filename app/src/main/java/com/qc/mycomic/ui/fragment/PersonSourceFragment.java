package com.qc.mycomic.ui.fragment;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.setting.Setting;
import com.qc.mycomic.setting.SettingFactory;
import com.qc.mycomic.util.PopupUtil;
import com.qc.mycomic.util.SourceUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.List;

import the.one.base.ui.fragment.BaseGroupListFragment;
import the.one.base.util.ToastUtil;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/1 11:14
 * @ver 1.0
 */
public class PersonSourceFragment extends BaseGroupListFragment implements View.OnClickListener {

    private QMUICommonListItemView v1, v2, v3, v4, v5, v6;

    @Override
    protected void initView(View view) {
        super.initView(view);
        QMUIQQFaceView mTitle = mTopLayout.setTitle("漫画源配置");
        mTopLayout.setTitleGravity(Gravity.CENTER);
        mTitle.setTextColor(getColorr(R.color.qmui_config_color_gray_1));
        mTitle.getPaint().setFakeBoldText(true);
        addTopBarBackBtn();
    }

    @Override
    protected void addGroupListView() {
        v1 = CreateDetailItemView("默认漫画源", SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE).getDetailDesc());
        v2 = CreateDetailItemView("选用漫画源", String.valueOf(SourceUtil.getSourceStrSize()));
//        addToGroup("漫画源设置", v1, v2);
        addToGroup(v1, v2);
    }

    @Override
    public void onClick(View view) {
        if (view == v1) {
            Setting setting = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE);
            PopupUtil.showSimpleBottomSheetList(getContext(), setting.getMyMap(), "选择默认漫画源", setting.getData(), new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    setting.setData(setting.getMyMap().getKeyByValue(tag));
                    String sourceStr = SourceUtil.getSourceStr();
                    StringBuilder strBuilder = new StringBuilder(sourceStr);
                    strBuilder.setCharAt(position, '1');
                    sourceStr = strBuilder.toString();
                    SourceUtil.reloadSourceList(sourceStr);
                    v1.setDetailText(tag);
                    v2.setDetailText(String.valueOf(SourceUtil.getSourceStrSize(sourceStr)));
                    dialog.dismiss();
                }
            });
        } else if (view == v2) {
            String[] items = SourceUtil.getAllSourceNameArray();
            int[] checkedItems = SourceUtil.getSourceIntArr();
            Setting defaultSetting = SettingFactory.getInstance().getSetting(SettingFactory.SETTING_DEFAULT_SOURCE);
            String sourceName = SourceUtil.getSourceName(Integer.parseInt(defaultSetting.getData()));
            int defaultIndex = -1;
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(sourceName)) {
                    defaultIndex = i;
                }
            }
            int finalDefaultIndex = defaultIndex;
            final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getContext())
                    .setCheckedItems(checkedItems)
                    .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
                    .addItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.addAction("取消", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    dialog.dismiss();
                }
            });
            builder.addAction("确认", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    int[] checkedItemIndexes = builder.getCheckedItemIndexes();
                    List<Integer> list = new ArrayList<>();
                    for (int checkedItemIndex : checkedItemIndexes) {
                        list.add(checkedItemIndex);
                    }
                    if (!list.contains(finalDefaultIndex)) {
                        ToastUtil.show("默认漫画源不可取消！");
                        list.add(finalDefaultIndex);
                    }
                    StringBuilder sourceStr = new StringBuilder();
                    for (int i = 0; i < SourceUtil.maxSize(); i++) {
                        if (list.contains(i)) {
                            sourceStr.append("1");
                        } else {
                            sourceStr.append("0");
                        }
                    }
                    SourceUtil.reloadSourceList(sourceStr.toString());
                    v2.setDetailText(String.valueOf(SourceUtil.getSourceStrSize(sourceStr.toString())));
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}
