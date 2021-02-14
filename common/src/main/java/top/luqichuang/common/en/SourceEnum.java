package top.luqichuang.common.en;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.mycomic.model.Source;
import top.luqichuang.mycomic.source.BiliBili;
import top.luqichuang.mycomic.source.Du;
import top.luqichuang.mycomic.source.OH;
import top.luqichuang.mycomic.source.BL;
import top.luqichuang.mycomic.source.MH118;
import top.luqichuang.mycomic.source.ManHuaFen;
import top.luqichuang.mycomic.source.ManHuaTai;
import top.luqichuang.mycomic.source.MiTui;
import top.luqichuang.mycomic.source.PuFei;
import top.luqichuang.mycomic.source.TengXun;

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
    BL(10, "BL漫画"),
    ;

    private static final Map<Integer, Source> MAP = new LinkedHashMap<>();

    static {
        MAP.put(MI_TUI.ID, new MiTui());
        MAP.put(MAN_HUA_FEN.ID, new ManHuaFen());
        MAP.put(PU_FEI.ID, new PuFei());
        MAP.put(TENG_XUN.ID, new TengXun());
        MAP.put(BILI_BILI.ID, new BiliBili());
        MAP.put(OH.ID, new OH());
        MAP.put(MAN_HUA_TAI.ID, new ManHuaTai());
        MAP.put(MH_118.ID, new MH118());
        MAP.put(DU.ID, new Du());
        MAP.put(BL.ID, new BL());
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
