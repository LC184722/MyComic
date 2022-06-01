package top.luqichuang.common.en;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.common.model.Source;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.source.AiYouMan;
import top.luqichuang.mycomic.source.BL;
import top.luqichuang.mycomic.source.BaoZi;
import top.luqichuang.mycomic.source.BiliBili;
import top.luqichuang.mycomic.source.DaShu;
import top.luqichuang.mycomic.source.Du;
import top.luqichuang.mycomic.source.MH118;
import top.luqichuang.mycomic.source.MH118W;
import top.luqichuang.mycomic.source.MH1234;
import top.luqichuang.mycomic.source.ManHuaTai;
import top.luqichuang.mycomic.source.MiTui;
import top.luqichuang.mycomic.source.OH;
import top.luqichuang.mycomic.source.PuFei;
import top.luqichuang.mycomic.source.QiMiao;
import top.luqichuang.mycomic.source.SiSi;
import top.luqichuang.mycomic.source.TengXun;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/11 23:51
 * @ver 1.0
 */
public enum SourceEnum {
    MI_TUI(1, "米推漫画"),
    //    MAN_HUA_FEN(2, "漫画粉"),
    PU_FEI(3, "扑飞漫画"),
    TENG_XUN(4, "腾讯动漫"),
    BILI_BILI(5, "哔哩哔哩"),
    OH(6, "OH漫画"),
    MAN_HUA_TAI(7, "漫画台"),
    MH_118(8, "118漫画"),
    DU(9, "独漫画"),
    BL(10, "BL漫画"),
    AI_YOU_MAN(11, "爱优漫"),
    MH_1234(12, "1234漫画"),
    MH_118_2(13, "118漫画[2]"),
    QI_MIAO(14, "奇妙漫画"),
    DA_SHU(15, "大树漫画"),
    SI_SI(16, "思思漫画"),
    BAO_ZI(17, "包子漫画"),
    ;

    private static final Map<Integer, Source<ComicInfo>> MAP = new LinkedHashMap<>();

    static {
        MAP.put(MI_TUI.ID, new MiTui());
//        MAP.put(MAN_HUA_FEN.ID, new ManHuaFen());
        MAP.put(PU_FEI.ID, new PuFei());
        MAP.put(TENG_XUN.ID, new TengXun());
        MAP.put(BILI_BILI.ID, new BiliBili());
        MAP.put(OH.ID, new OH());
        MAP.put(MAN_HUA_TAI.ID, new ManHuaTai());
        MAP.put(MH_118.ID, new MH118());
        MAP.put(DU.ID, new Du());
        MAP.put(BL.ID, new BL());
        MAP.put(AI_YOU_MAN.ID, new AiYouMan());
        MAP.put(MH_1234.ID, new MH1234());
        MAP.put(MH_118_2.ID, new MH118W());
        MAP.put(QI_MIAO.ID, new QiMiao());
        MAP.put(DA_SHU.ID, new DaShu());
        MAP.put(SI_SI.ID, new SiSi());
        MAP.put(BAO_ZI.ID, new BaoZi());
    }

    public static Map<Integer, Source<ComicInfo>> getMAP() {
        return MAP;
    }

    public final int ID;
    public final String NAME;

    SourceEnum(int id, String name) {
        this.ID = id;
        this.NAME = name;
    }
}
