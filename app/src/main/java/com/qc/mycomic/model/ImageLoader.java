package com.qc.mycomic.model;

import com.qc.mycomic.view.ReaderView;

public interface ImageLoader {

    void loadImageInfoList(ReaderView view, String chapterUrl, int chapterId);

}
