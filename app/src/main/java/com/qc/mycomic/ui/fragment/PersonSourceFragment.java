package com.qc.mycomic.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.mycomic.R;
import com.qc.mycomic.en.SettingEnum;
import top.luqichuang.common.mycomic.util.MapUtil;
import com.qc.mycomic.util.PopupUtil;
import com.qc.mycomic.util.SettingItemUtil;
import com.qc.mycomic.util.SettingUtil;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.Map;

import the.one.base.ui.fragment.BaseGroupListFragment;

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
        v1 = CreateDetailItemView("默认漫画源", SettingUtil.getSettingDesc(SettingEnum.DEFAULT_SOURCE));
//        v2 = CreateDetailItemView("选用漫画源", String.valueOf(SourceUtil.size()));
//        addToGroup("漫画源设置", v1, v2);
        addToGroup(v1);
    }

    @Override
    public void onClick(View view) {
        if (view == v1) {
            Object key = SettingUtil.getSettingKey(SettingEnum.DEFAULT_SOURCE);
            Map<Object, String> map = SettingItemUtil.getMap(SettingEnum.DEFAULT_SOURCE);
            PopupUtil.showSimpleBottomSheetList(getContext(), map, key, "选择默认漫画源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                @Override
                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                    Object key = MapUtil.getKeyByValue(map, tag);
                    SettingUtil.putSetting(SettingEnum.DEFAULT_SOURCE, key, tag);
                    v1.setDetailText(tag);
                    dialog.dismiss();
                }
            });
//        } else if (view == v2) {
//            String[] items = SourceUtil.getAllSourceNameArray();
//            int[] checkedItems = SourceUtil.getSourceIntArr();
//            Setting defaultSetting = SettingFactory.getInstance().getSettingKey(SettingFactory.SETTING_DEFAULT_SOURCE);
//            String sourceName = SourceUtil.sourceName(Integer.parseInt(defaultSetting.getData()));
//            int defaultIndex = -1;
//            for (int i = 0; i < items.length; i++) {
//                if (items[i].equals(sourceName)) {
//                    defaultIndex = i;
//                }
//            }
//            int finalDefaultIndex = defaultIndex;
//            final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getContext())
//                    .setCheckedItems(checkedItems)
//                    .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
//                    .addItems(items, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//            builder.addAction("取消", new QMUIDialogAction.ActionListener() {
//                @Override
//                public void onClick(QMUIDialog dialog, int index) {
//                    dialog.dismiss();
//                }
//            });
//            builder.addAction("确认", new QMUIDialogAction.ActionListener() {
//                @Override
//                public void onClick(QMUIDialog dialog, int index) {
//                    int[] checkedItemIndexes = builder.getCheckedItemIndexes();
//                    List<Integer> list = new ArrayList<>();
//                    for (int checkedItemIndex : checkedItemIndexes) {
//                        list.add(checkedItemIndex);
//                    }
//                    if (!list.contains(finalDefaultIndex)) {
//                        ToastUtil.show("默认漫画源不可取消！");
//                        list.add(finalDefaultIndex);
//                    }
//                    StringBuilder sourceStr = new StringBuilder();
//                    for (int i = 0; i < SourceUtil.maxSize(); i++) {
//                        if (list.contains(i)) {
//                            sourceStr.append("1");
//                        } else {
//                            sourceStr.append("0");
//                        }
//                    }
//                    SourceUtil.reloadSourceList(sourceStr.toString());
//                    v2.setDetailText(String.valueOf(SourceUtil.getSourceStrSize(sourceStr.toString())));
//                    dialog.dismiss();
//                }
//            });
//            builder.show();
        }
    }
}
