package com.qc.mycomic.model;

import java.util.List;
import java.util.Map;

public interface Source {

    int getSourceId();

    String getSourceName();

    String getIndex();

    String getSearchUrl(String searchString);

    List<ComicInfo> getComicInfoList(String html);

    void setComicDetail(ComicInfo comicInfo, String html);

    List<ImageInfo> getImageInfoList(String html, int chapterId);

    MyMap<String, String> getRankMap();

    List<ComicInfo> getRankComicInfoList(String html);
}
