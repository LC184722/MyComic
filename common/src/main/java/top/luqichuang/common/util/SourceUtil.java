package top.luqichuang.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.luqichuang.common.en.NSourceEnum;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.common.en.VSourceEnum;
import top.luqichuang.common.model.Source;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mynovel.model.NovelInfo;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/10 15:51
 * @ver 1.0
 */
public class SourceUtil {

    private static final Map<Integer, Source<ComicInfo>> COMIC_MAP = SourceEnum.getMAP();
    private static final Map<Integer, Source<NovelInfo>> NOVEL_MAP = NSourceEnum.getMAP();
    private static final Map<Integer, Source<VideoInfo>> VIDEO_MAP = VSourceEnum.getMAP();

    private static final Map<Integer, String> COMIC_NAME_MAP = new HashMap<>();
    private static final Map<Integer, String> NOVEL_NAME_MAP = new HashMap<>();
    private static final Map<Integer, String> VIDEO_NAME_MAP = new HashMap<>();

    private static final List<Source<ComicInfo>> COMIC_SOURCE_LIST = new ArrayList<>();
    private static final List<Source<NovelInfo>> NOVEL_SOURCE_LIST = new ArrayList<>();
    private static final List<Source<VideoInfo>> VIDEO_SOURCE_LIST = new ArrayList<>();

    private static final List<String> COMIC_SOURCE_NAME_LIST = new ArrayList<>();
    private static final List<String> NOVEL_SOURCE_NAME_LIST = new ArrayList<>();
    private static final List<String> VIDEO_SOURCE_NAME_LIST = new ArrayList<>();

    public static void init() {
        COMIC_NAME_MAP.clear();
        COMIC_SOURCE_LIST.clear();
        COMIC_SOURCE_NAME_LIST.clear();
        NOVEL_NAME_MAP.clear();
        NOVEL_SOURCE_LIST.clear();
        NOVEL_SOURCE_NAME_LIST.clear();
        VIDEO_NAME_MAP.clear();
        VIDEO_SOURCE_LIST.clear();
        VIDEO_SOURCE_NAME_LIST.clear();
        for (Source<ComicInfo> source : COMIC_MAP.values()) {
            COMIC_NAME_MAP.put(source.getSourceId(), source.getSourceName());
            if (source.isValid()) {
                COMIC_SOURCE_LIST.add(source);
                COMIC_SOURCE_NAME_LIST.add(source.getSourceName());
            }
        }
        for (Source<NovelInfo> source : NOVEL_MAP.values()) {
            NOVEL_NAME_MAP.put(source.getSourceId(), source.getSourceName());
            if (source.isValid()) {
                NOVEL_SOURCE_LIST.add(source);
                NOVEL_SOURCE_NAME_LIST.add(source.getSourceName());
            }
        }
        for (Source<VideoInfo> source : VIDEO_MAP.values()) {
            VIDEO_NAME_MAP.put(source.getSourceId(), source.getSourceName());
            if (source.isValid()) {
                VIDEO_SOURCE_LIST.add(source);
                VIDEO_SOURCE_NAME_LIST.add(source.getSourceName());
            }
        }
    }

    public static Source<ComicInfo> getSource(int sourceId) {
        return COMIC_MAP.get(sourceId);
    }

    public static Source<NovelInfo> getNSource(int sourceId) {
        return NOVEL_MAP.get(sourceId);
    }

    public static Source<VideoInfo> getVSource(int sourceId) {
        return VIDEO_MAP.get(sourceId);
    }

    public static String getSourceName(int sourceId) {
        Source<ComicInfo> source = COMIC_MAP.get(sourceId);
        if (source != null) {
            return source.getSourceName();
        }
        return null;
    }

    public static String getNSourceName(int sourceId) {
        Source<NovelInfo> source = NOVEL_MAP.get(sourceId);
        if (source != null) {
            return source.getSourceName();
        }
        return null;
    }

    public static String getVSourceName(int sourceId) {
        Source<VideoInfo> source = VIDEO_MAP.get(sourceId);
        if (source != null) {
            return source.getSourceName();
        }
        return null;
    }

    public static Integer getSourceId(String name) {
        return MapUtil.getKeyByValue(COMIC_NAME_MAP, name);
    }

    public static Integer getNSourceId(String name) {
        return MapUtil.getKeyByValue(NOVEL_NAME_MAP, name);
    }

    public static Integer getVSourceId(String name) {
        return MapUtil.getKeyByValue(VIDEO_NAME_MAP, name);
    }

    public static List<Source<ComicInfo>> getSourceList() {
        return COMIC_SOURCE_LIST;
    }

    public static List<Source<NovelInfo>> getNSourceList() {
        return NOVEL_SOURCE_LIST;
    }

    public static List<Source<VideoInfo>> getVSourceList() {
        return VIDEO_SOURCE_LIST;
    }

    public static List<String> getComicSourceNameList() {
        return COMIC_SOURCE_NAME_LIST;
    }

    public static List<String> getNovelSourceNameList() {
        return NOVEL_SOURCE_NAME_LIST;
    }

    public static List<String> getVideoSourceNameList() {
        return VIDEO_SOURCE_NAME_LIST;
    }

    public static int size() {
        return COMIC_SOURCE_LIST.size();
    }

    public static int nSize() {
        return NOVEL_SOURCE_LIST.size();
    }

    public static int vSize() {
        return VIDEO_SOURCE_LIST.size();
    }

}
