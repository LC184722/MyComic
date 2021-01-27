package top.luqichuang.common.mycomic.source;

import top.luqichuang.common.mycomic.en.SourceEnum;
import top.luqichuang.common.mycomic.model.ComicInfo;
import top.luqichuang.common.mycomic.model.ImageInfo;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

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
    public List<ImageInfo> getImageInfoList(String html, int chapterId) {
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
