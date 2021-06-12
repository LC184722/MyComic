package com.qc.common.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qc.common.constant.TmpData;
import com.qc.common.en.SettingEnum;
import com.qc.common.ui.adapter.NovelReaderAdapter;
import com.qc.common.util.SettingUtil;
import com.qc.mycomic.R;

import the.one.base.widge.TheCheckBox;
import top.luqichuang.common.model.Entity;
import top.luqichuang.mynovel.model.NovelInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/11 9:48
 * @ver 1.0
 */
public class NovelReaderFragment extends BaseReaderFragment {

    private NovelReaderAdapter novelReaderAdapter;

    public static NovelReaderFragment getInstance(Entity entity) {
        NovelReaderFragment fragment = new NovelReaderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        novelReaderAdapter = new NovelReaderAdapter(R.layout.item_reader_novel, (NovelInfo) entityInfo);
    }

    @Override
    protected int getSettingsViewId() {
        return R.layout.fragment_reader_settings_novel;
    }

    @Override
    protected void setSettingsView(View settingsView) {
        LinearLayout llSettingsContent = settingsView.findViewById(R.id.llSettingsContent);
        llSettingsContent.setOnClickListener(v -> {
        });

        LinearLayout llCancel = settingsView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(v -> {
            hideView(settingsView);
        });

        LinearLayout llFull = settingsView.findViewById(R.id.llFull);
        TheCheckBox checkBox = llFull.findViewById(R.id.checkBox);
        checkBox.setIsCheckDrawable(R.drawable.ic_baseline_check_circle_24);
        checkBox.setCheck(TmpData.isFull);
        checkBox.setOnClickListener(v -> {
            TmpData.isFull = !checkBox.isCheck();
            SettingUtil.putSetting(SettingEnum.IS_FULL_SCREEN, TmpData.isFull);
            checkBox.setCheck(TmpData.isFull);
        });
        llFull.setOnClickListener(v -> {
            TmpData.isFull = !checkBox.isCheck();
            SettingUtil.putSetting(SettingEnum.IS_FULL_SCREEN, TmpData.isFull);
            checkBox.setCheck(TmpData.isFull);
        });

        LinearLayout llFont = settingsView.findViewById(R.id.llFont);
        TextView tvSub = llFont.findViewById(R.id.tvSub);
        TextView tvAdd = llFont.findViewById(R.id.tvAdd);
        TextView tvFont = llFont.findViewById(R.id.tvFont);
        tvSub.setOnClickListener(v -> {
            tvFont.setText(novelReaderAdapter.subFont());
        });
        tvAdd.setOnClickListener(v -> {
            tvFont.setText(novelReaderAdapter.addFont());
        });
        tvFont.setText(novelReaderAdapter.getFontSizeDesc());

        TheCheckBox checkBoxAuto = settingsView.findViewById(R.id.checkBoxAuto);
        checkBoxAuto.setIsCheckDrawable(R.drawable.ic_baseline_check_circle_24);
        checkBoxAuto.setOnClickListener(v -> {
            checkBoxAuto.setCheck(true);
            recycleView.smoothScrollToPosition(first + 1);
            _mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        });
        TextView tvAutoSub = settingsView.findViewById(R.id.tvAutoSub);
        TextView tvAutoAdd = settingsView.findViewById(R.id.tvAutoAdd);
        TextView tvAuto = settingsView.findViewById(R.id.tvAuto);
        layoutManager.changeSpeed((Integer) SettingUtil.getSettingKey(SettingEnum.NOVEL_AUTO_SPEED));
        tvAuto.setText(layoutManager.getSpeedDesc());
        tvAutoSub.setOnClickListener(v -> {
            tvAuto.setText(layoutManager.subSpeed());
        });
        tvAutoAdd.setOnClickListener(v -> {
            tvAuto.setText(layoutManager.addSpeed());
        });
    }

    @Override
    protected void firstLoadView() {
        bottomView.findViewById(R.id.seekBar).setVisibility(View.GONE);
        topView.findViewById(R.id.tvProgress).setVisibility(View.GONE);
        bottomView.findViewById(R.id.tvChapterProgress).setVisibility(View.GONE);
        TextView tvTitleCenter = bottomView.findViewById(R.id.tvTitleCenter);
        tvTitleCenter.setText(entity.getTitle());
        tvTitleCenter.setVisibility(View.VISIBLE);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return novelReaderAdapter;
    }

    @Override
    public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        return true;
    }
}
