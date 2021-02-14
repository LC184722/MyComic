package top.luqichuang.common.util;

import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.mycomic.model.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LuQiChuang
 * @desc 漫画资源工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class SourceUtil {

    private static final Map<Integer, Source> MAP = SourceEnum.getMAP();

    private static final List<Source> SOURCE_LIST = new ArrayList<>();

    static {
        for (Source source : MAP.values()) {
            if (source.isValid()) {
                SOURCE_LIST.add(source);
            }
        }
    }

    public static Source getSource(int sourceId) {
        return MAP.get(sourceId);
    }

    public static String getSourceName(int sourceId) {
        Source source = MAP.get(sourceId);
        if (source != null) {
            return source.getSourceName();
        }
        return null;
    }

    public static List<Source> getSourceList() {
        return SOURCE_LIST;
    }

    public static int size() {
        return SOURCE_LIST.size();
    }
}
