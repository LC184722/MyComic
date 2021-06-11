package top.luqichuang.common.en;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.common.model.Source;
import top.luqichuang.mynovel.model.NovelInfo;
import top.luqichuang.mynovel.source.AiYue;
import top.luqichuang.mynovel.source.K17;
import top.luqichuang.mynovel.source.XinBiQuGe;
import top.luqichuang.mynovel.source.XuanShu;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/11 23:51
 * @ver 1.0
 */
public enum NSourceEnum {

    EMPTY(-1, "空"),
    XIN_BI_QU_GE(1, "新笔趣阁"),
    QUAN_SHU(2, "全书网"),
    QUAN_XIAO_SHUO(3, "全小说"),
    AI_YUE(4, "爱阅小说"),
    XUAN_SHU(5, "炫书网"),
    K17(6, "17K小说"),
    ;

    private static final Map<Integer, Source<NovelInfo>> MAP = new LinkedHashMap<>();

    static {
        MAP.put(XIN_BI_QU_GE.ID, new XinBiQuGe());
//        MAP.put(QUAN_SHU.ID, new QuanShu());
//        MAP.put(QUAN_XIAO_SHUO.ID, new QuanXiaoShuo());
        MAP.put(AI_YUE.ID, new AiYue());
        MAP.put(XUAN_SHU.ID, new XuanShu());
        MAP.put(K17.ID, new K17());
    }

    public static Map<Integer, Source<NovelInfo>> getMAP() {
        return MAP;
    }

    public final int ID;
    public final String NAME;

    NSourceEnum(int id, String name) {
        this.ID = id;
        this.NAME = name;
    }
}
