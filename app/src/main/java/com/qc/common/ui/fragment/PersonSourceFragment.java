package com.qc.common.ui.fragment;

import android.view.Gravity;
import android.view.View;

import com.qc.common.en.SettingEnum;
import com.qc.common.util.EntityUtil;
import com.qc.common.util.PopupUtil;
import com.qc.common.util.SettingItemUtil;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import the.one.base.ui.fragment.BaseGroupListFragment;
import the.one.base.util.QMUIDialogUtil;
import top.luqichuang.common.util.MapUtil;
import top.luqichuang.common.util.SourceUtil;

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
        v2 = CreateDetailItemView("默认小说源", SettingUtil.getSettingDesc(SettingEnum.DEFAULT_NOVEL_SOURCE));
        v3 = CreateDetailItemView("默认番剧源", SettingUtil.getSettingDesc(SettingEnum.DEFAULT_VIDEO_SOURCE));
        v4 = CreateDetailItemView("漫画源启用");
        v5 = CreateDetailItemView("小说源启用");
        v6 = CreateDetailItemView("番剧源启用");
        addToGroup("漫画", v1, v4);
        addToGroup("小说", v2, v5);
        addToGroup("番剧", v3, v6);
    }

    private void chooseDefaultSource(SettingEnum settingEnum, String type, QMUICommonListItemView view) {
        Object key = SettingUtil.getSettingKey(settingEnum);
        Map<Object, String> map = SettingItemUtil.getMap(settingEnum);
        PopupUtil.showSimpleBottomSheetList(getContext(), map, key, "选择默认" + type + "源", new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
            @Override
            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                Object key = MapUtil.getKeyByValue(map, tag);
                SettingUtil.putSetting(settingEnum, key, tag);
                view.setDetailText(tag);
                dialog.dismiss();
            }
        });
    }

    private int[] getChooseArray(List<String> sourceNameList, List<String> openNameList) {
        List<Integer> list = new ArrayList<>();
        int index = 0;
        for (String name : sourceNameList) {
            for (String s : openNameList) {
                if (Objects.equals(name, s)) {
                    list.add(index);
                    break;
                }
            }
            index++;
        }
        int[] results = new int[list.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = list.get(i);
        }
        return results;
    }

    private String[] getArray(List<String> list) {
        return list.toArray(new String[0]);
    }

    private void chooseOpenSource(List<String> sourceNameList, SettingEnum settingEnum) {
        String[] ss = getArray(sourceNameList);
        Collection<Integer> ids = (Collection<Integer>) SettingUtil.getSettingKey(settingEnum);
        List<String> openNameList = new ArrayList<>();
        for (Integer id : ids) {
            if (settingEnum == SettingEnum.COMIC_SOURCE_OPEN) {
                openNameList.add(SourceUtil.getSourceName(id));
            } else if (settingEnum == SettingEnum.NOVEL_SOURCE_OPEN) {
                openNameList.add(SourceUtil.getNSourceName(id));
            } else if (settingEnum == SettingEnum.VIDEO_SOURCE_OPEN) {
                openNameList.add(SourceUtil.getVSourceName(id));
            }
        }
        int[] is = getChooseArray(sourceNameList, openNameList);

        QMUIDialogUtil.showMultiChoiceDialog(getContext(), ss, is, "确定", new QMUIDialogUtil.OnMultiChoiceConfirmClickListener() {
            @Override
            public void getCheckedItemIndexes(QMUIDialog dialog, int[] checkedItems) {
                List<Integer> list = new ArrayList<>();
                for (int checkedItem : checkedItems) {
                    if (settingEnum == SettingEnum.COMIC_SOURCE_OPEN) {
                        list.add(SourceUtil.getSourceId(ss[checkedItem]));
                    } else if (settingEnum == SettingEnum.NOVEL_SOURCE_OPEN) {
                        list.add(SourceUtil.getNSourceId(ss[checkedItem]));
                    } else if (settingEnum == SettingEnum.VIDEO_SOURCE_OPEN) {
                        list.add(SourceUtil.getVSourceId(ss[checkedItem]));
                    }
                }
                int defaultSourceId = 1;
                String title = "";
                if (settingEnum == SettingEnum.COMIC_SOURCE_OPEN) {
                    defaultSourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_SOURCE);
                    title = SourceUtil.getSourceName(defaultSourceId);
                } else if (settingEnum == SettingEnum.NOVEL_SOURCE_OPEN) {
                    defaultSourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_NOVEL_SOURCE);
                    title = SourceUtil.getNSourceName(defaultSourceId);
                } else if (settingEnum == SettingEnum.VIDEO_SOURCE_OPEN) {
                    defaultSourceId = (int) SettingUtil.getSettingKey(SettingEnum.DEFAULT_VIDEO_SOURCE);
                    title = SourceUtil.getVSourceName(defaultSourceId);
                }
                if (!list.contains(defaultSourceId)) {
                    showToast("默认数据源:[" + title + "]不可关闭！");
                } else {
                    SettingUtil.putSetting(settingEnum, list);
                    EntityUtil.initEntityList(EntityUtil.STATUS_ALL);
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == v1) {
            chooseDefaultSource(SettingEnum.DEFAULT_SOURCE, "漫画", v1);
        } else if (view == v2) {
            chooseDefaultSource(SettingEnum.DEFAULT_NOVEL_SOURCE, "小说", v2);
        } else if (view == v3) {
            chooseDefaultSource(SettingEnum.DEFAULT_VIDEO_SOURCE, "番剧", v3);
        } else if (view == v4) {
            chooseOpenSource(SourceUtil.getComicSourceNameList(), SettingEnum.COMIC_SOURCE_OPEN);
        } else if (view == v5) {
            chooseOpenSource(SourceUtil.getNovelSourceNameList(), SettingEnum.NOVEL_SOURCE_OPEN);
        } else if (view == v6) {
            chooseOpenSource(SourceUtil.getVideoSourceNameList(), SettingEnum.VIDEO_SOURCE_OPEN);
        }
    }
}
