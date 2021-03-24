package top.luqichuang.mycomic.source;

import java.util.List;
import java.util.Map;

import okhttp3.Request;
import top.luqichuang.common.en.SourceEnum;
import top.luqichuang.mycomic.model.BaseSource;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mycomic.model.ImageInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/1/12 22:51
 * @ver 1.0
 */
public class EmptySource extends BaseSource {

    @Override
    public SourceEnum getSourceEnum() {
        return SourceEnum.EMPTY;
    }

    @Override
    public String getIndex() {
        return null;
    }

    @Override
    public Request getSearchRequest(String searchString) {
        return null;
    }

    @Override
    public List<ComicInfo> getComicInfoList(String html) {
        return null;
    }

    @Override
    public void setComicDetail(ComicInfo comicInfo, String html) {

    }

    @Override
    public List<ImageInfo> getImageInfoList(String html, int chapterId, Map<String, Object> map) {
        return null;
    }

    @Override
    public Map<String, String> getRankMap() {
        return null;
    }

    @Override
    public List<ComicInfo> getRankComicInfoList(String html) {
        return null;
    }
}
