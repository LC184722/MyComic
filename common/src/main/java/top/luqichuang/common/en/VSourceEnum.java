package top.luqichuang.common.en;

import java.util.LinkedHashMap;
import java.util.Map;

import top.luqichuang.common.model.Source;
import top.luqichuang.myvideo.model.VideoInfo;
import top.luqichuang.myvideo.source.MiLiMiLi;
import top.luqichuang.myvideo.source.YingHua;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/22 23:47
 * @ver 1.0
 */
public enum VSourceEnum {
    YING_HUA(1, "樱花动漫"),
    MILI_MILI(2, "米粒米粒"),
    ;

    private static final Map<Integer, Source<VideoInfo>> MAP = new LinkedHashMap<>();

    static {
        MAP.put(YING_HUA.ID, new YingHua());
        MAP.put(MILI_MILI.ID, new MiLiMiLi());
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
