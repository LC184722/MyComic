package com.qc.mycomic.en;

import com.qc.mycomic.model.Source;
import com.qc.mycomic.source.BiliBili;
import com.qc.mycomic.source.Du;
import com.qc.mycomic.source.MH118;
import com.qc.mycomic.source.ManHuaFen;
import com.qc.mycomic.source.ManHuaTai;
import com.qc.mycomic.source.MiTui;
import com.qc.mycomic.source.OH;
import com.qc.mycomic.source.PuFei;
import com.qc.mycomic.source.TengXun;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/11 23:51
 * @ver 1.0
 */
public enum SourceEnum {

    EMPTY(-1, "空"),
    MI_TUI(1, "米推漫画"),
    MAN_HUA_FEN(2, "漫画粉"),
    PU_FEI(3, "扑飞漫画"),
    TENG_XUN(4, "腾讯动漫"),
    BILI_BILI(5, "哔哩哔哩"),
    OH(6, "OH漫画"),
    MAN_HUA_TAI(7, "漫画台"),
    MH_118(8, "118漫画"),
    DU(9, "独漫画"),
    ;

    private static final Map<Integer, Source> MAP;

    static {
        MAP = new LinkedHashMap<>();
        MAP.put(MI_TUI.ID, new MiTui());
        MAP.put(MAN_HUA_FEN.ID, new ManHuaFen());
        MAP.put(PU_FEI.ID, new PuFei());
        MAP.put(TENG_XUN.ID, new TengXun());
        MAP.put(BILI_BILI.ID, new BiliBili());
        MAP.put(OH.ID, new OH());
        MAP.put(MAN_HUA_TAI.ID, new ManHuaTai());
        MAP.put(MH_118.ID, new MH118());
        MAP.put(DU.ID, new Du());
    }

    public static Map<Integer, Source> getMAP() {
        return MAP;
    }

    public final int ID;
    public final String NAME;

    SourceEnum(int id, String name) {
        this.ID = id;
        this.NAME = name;
    }
}
