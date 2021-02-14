package top.luqichuang.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.mynovel.model.NSource;


/**
 * @author LuQiChuang
 * @desc 漫画资源工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class NSourceUtil {

    private static final Map<Integer, NSource> MAP = NSourceEnum.getMAP();

    private static final List<NSource> SOURCE_LIST = new ArrayList<>();

    static {
        for (NSource nSource : MAP.values()) {
            if (nSource.isValid()) {
                SOURCE_LIST.add(nSource);
            }
        }
    }

    public static NSource getNSource(int nSourceId) {
        return MAP.get(nSourceId);
    }

    public static String getNSourceName(int nSourceId) {
        NSource nSource = MAP.get(nSourceId);
        if (nSource != null) {
            return nSource.getNSourceName();
        }
        return null;
    }

    public static List<NSource> getNSourceList() {
        return SOURCE_LIST;
    }

    public static int size() {
        return SOURCE_LIST.size();
    }
}
