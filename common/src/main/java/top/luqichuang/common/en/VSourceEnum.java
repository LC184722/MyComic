package top.luqichuang.common.en;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.common.model.Source;
import top.luqichuang.myvideo.model.VideoInfo;
import top.luqichuang.myvideo.source.AiYun;
import top.luqichuang.myvideo.source.BiliBili;
import top.luqichuang.myvideo.source.FengChe;
import top.luqichuang.myvideo.source.FengChe2;
import top.luqichuang.myvideo.source.MiLiMiLi;
import top.luqichuang.myvideo.source.YingHua;
import top.luqichuang.myvideo.source.YingHua2;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/22 23:47
 * @ver 1.0
 */
public enum VSourceEnum {
    YING_HUA(1, "樱花动漫"),
    MILI_MILI(2, "米粒米粒"),
    FENG_CHE(3, "风车动漫"),
    YING_HUA_2(4, "樱花动漫[2]"),
    BILI_BILI(5, "哔哩哔哩"),
    FENG_CHE_2(6, "风车动漫[2]"),
    AI_YUN(7, "爱云影视"),
    ;

    private static final Map<Integer, Source<VideoInfo>> MAP = new LinkedHashMap<>();

    static {
        MAP.put(YING_HUA.ID, new YingHua());
        MAP.put(MILI_MILI.ID, new MiLiMiLi());
        MAP.put(FENG_CHE.ID, new FengChe());
        MAP.put(YING_HUA_2.ID, new YingHua2());
        MAP.put(BILI_BILI.ID, new BiliBili());
        MAP.put(FENG_CHE_2.ID, new FengChe2());
        MAP.put(AI_YUN.ID, new AiYun());
    }

    public static Map<Integer, Source<VideoInfo>> getMAP() {
        return MAP;
    }

    public final int ID;
    public final String NAME;

    VSourceEnum(int id, String name) {
        this.ID = id;
        this.NAME = name;
    }
}
