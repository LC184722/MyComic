package com.qc.mycomic.model;

import java.util.List;
import java.util.Map;

import okhttp3.Request;

public interface Source {

    int getSourceId();

    String getSourceName();

    String getIndex();

    Request getSearchRequest(String searchString);

    Request getDetailRequest(String detailUrl);

    Request getRankRequest(String rankUrl);

    List<ComicInfo> getComicInfoList(String html);

    void setComicDetail(ComicInfo comicInfo, String html);

    List<ImageInfo> getImageInfoList(String html, int chapterId);

    MyMap<String, String> getRankMap();

    List<ComicInfo> getRankComicInfoList(String html);
}
