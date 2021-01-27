package com.qc.mycomic.util;

import android.content.Context;

import com.qc.mycomic.constant.AppConstant;
import com.qc.mycomic.constant.Constant;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.luqichuang.common.mycomic.model.Comic;
import top.luqichuang.common.mycomic.model.ComicInfo;
import top.luqichuang.common.mycomic.util.SourceUtil;

/**
 * @author LuQiChuang
 * @desc 数据库连接工具
 * @date 2020/8/12 15:27
 * @ver 1.0
 */
public class DBUtil {

    public static final String TAG = "DBUtil";

    public static final int SAVE_ONLY = 0;
    public static final int SAVE_CUR = 1;
    public static final int SAVE_ALL = 2;

    public static final int STATUS_HIS = 0;
    public static final int STATUS_FAV = 1;
    public static final int STATUS_ALL = 2;

    /**
     * 保存漫画信息，默认保存所有漫画信息
     *
     * @param comic comic
     * @return void
     */
    public static void saveComic(Comic comic) {
        saveComic(comic, SAVE_ALL);
    }

    /**
     * 根据mode，保存漫画信息
     *
     * @param comic comic
     * @param mode  DBUtil.SAVE_* , only - 保存漫画信息，cur - 保存当前源信息，all - 保存所有源信息
     * @return void
     */
    public static void saveComic(Comic comic, int mode) {
        if (comic != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mode == SAVE_ONLY) {
                        saveComicData(comic);
                    } else if (mode == SAVE_CUR) {
                        saveComicData(comic);
                        saveComicInfoData(comic.getComicInfo());
                    } else {
                        saveComicData(comic);
                        for (ComicInfo comicInfo : comic.getComicInfoList()) {
                            saveComicInfoData(comicInfo);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 保存漫画源信息
     *
     * @param comicInfo comicInfo
     * @return void
     */
    public static void saveComicInfo(ComicInfo comicInfo) {
        if (comicInfo != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveComicInfoData(comicInfo);
                }
            }).start();
        }
    }

    private static void saveComicData(Comic comic) {
        if (comic != null) {
            if (comic.getTitle() == null) {
                comic.setTitle(comic.getComicInfo().getTitle());
                comic.setSourceId(comic.getComicInfo().getSourceId());
                comic.setDate(new Date());
                comic.setStatus(STATUS_HIS);
            }
            comic.saveOrUpdate("title = ?", comic.getTitle());
            //Log.i(TAG, "saveComicData: comic --> " + comic.getTitle());
        }
    }

    private static void saveComicInfoData(ComicInfo comicInfo) {
        if (comicInfo != null) {
            comicInfo.saveOrUpdate("title = ? and sourceId = ?", comicInfo.getTitle(), String.valueOf(comicInfo.getSourceId()));
            //Log.i(TAG, "saveComicInfoData: comicInfo --> " + comicInfo.getTitle() + " " + SourceUtil.getSOURCE_NAME(comicInfo.getSourceId()));
        }
    }

//    /**
//     * 保存该漫画信息及所有漫画源信息
//     *
//     * @param comic 保存漫画
//     * @return void
//     */
//    public static void saveData(Comic comic) {
//        saveData(comic, true);
//    }
//
//    /**
//     * 保存漫画信息
//     *
//     * @param comic    当前漫画
//     * @param needInfo 是否保存所有漫画源信息
//     * @return void
//     */
//    public static void saveData(Comic comic, boolean needInfo) {
//        if (comic != null) {
//            new Thread(() -> {
//                comic.saveOrUpdate("title = ?", comic.getTitle());
//                //Log.i(TAG, "saveComic: " + comic.getTitle() + " p = " + comic.getPriority());
//                if (!comic.getComicInfoList().isEmpty() && needInfo) {
//                    for (ComicInfo info : comic.getComicInfoList()) {
//                        DBUtil.saveData(info);
//                    }
//                }
//            }).start();
//        }
//    }
//
//    /**
//     * 保存漫画源信息
//     *
//     * @param comicInfo 当前漫画源
//     * @return void
//     */
//    public static void saveData(ComicInfo comicInfo) {
//        if (comicInfo != null) {
//            new Thread(() -> comicInfo.saveOrUpdate("title = ? and sourceId = ?", comicInfo.getTitle(), String.valueOf(comicInfo.getSourceId()))).start();
//            //Log.i(TAG, "saveComicInfo: " + comicInfo.getTitle() + "->" + SourceUtil.getSOURCE_NAME(comicInfo.getSourceId()));
//        }
//    }

    /**
     * 删除漫画
     *
     * @param comic 漫画
     * @return void
     */
    public static void deleteData(Comic comic) {
        if (comic != null) {
            new Thread(() -> {
                comic.delete();
                for (ComicInfo comicInfo : comic.getComicInfoList()) {
                    File file = new File(ImgUtil.getLocalImgUrl(comicInfo.getId()));
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }).start();
//            new Thread(comic::delete).start();
        }
    }

    /**
     * 根据status查询漫画
     *
     * @param status 漫画状态,0 - 收藏，1 - 历史，2 - 所有
     * @return List<Comic>
     */
    public static List<Comic> findComicListByStatus(int status) {
        List<Comic> list;
        String order = "priority DESC, date DESC";
        if (status == Constant.STATUS_ALL) {
            list = LitePal.order(order).find(Comic.class);
        } else {
            list = LitePal.where("status = ?", String.valueOf(status)).order(order).find(Comic.class);
        }
        List<Comic> dList = new ArrayList<>();
        for (Comic comic : list) {
            List<ComicInfo> infoList = findComicInfoListByTitle(comic.getTitle());
            for (ComicInfo info : infoList) {
                if (SourceUtil.getSource(info.getSourceId()).isValid()) {
                    comic.addComicInfo(info);
                    if (comic.getSourceId() == info.getSourceId()) {
                        comic.setComicInfo(info);
                    }
                    //更改detailUrl
                    String url = info.getDetailUrl();
                    String index = SourceUtil.getSource(info.getSourceId()).getIndex();
                    if (!url.startsWith(index)) {
                        String tmp = url.substring(url.indexOf('/', url.indexOf('.')));
                        url = index + tmp;
                        info.setDetailUrl(url);
                        saveComicInfo(info);
                    }
                    //end
                }
            }
            if (comic.getComicInfo() == null) {
                if (comic.getComicInfoList().isEmpty()) {
                    dList.add(comic);
                } else {
                    comic.setComicInfo(comic.getComicInfoList().get(0));
                }
            }
        }
        if (dList.size() > 0) {
            list.removeAll(dList);
        }
        return list;
    }

    /**
     * 根据漫画标题查找信息表里所有信息
     *
     * @param title 漫画标题
     * @return List<ComicInfo>
     */
    public static List<ComicInfo> findComicInfoListByTitle(String title) {
        return LitePal.where("title = ?", title).find(ComicInfo.class);
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        return LitePal.findAll(clazz);
    }


    public static boolean backupData(Context context) {
        return dealData(context, true);
    }

    public static boolean restoreData(Context context) {
        return dealData(context, false);
    }

    private static boolean dealData(Context context, boolean isBackup) {
        String dbFileName = "comic.db";
        String backupFileName = "backup_comic.db";
        String tmpFileName = "tmp.db";
        String path = AppConstant.APP_PATH;
        try {
            File dbFile = context.getDatabasePath(dbFileName);
            File backupFile = new File(path, backupFileName);

            if (!dbFile.exists()) {
                //Log.i(TAG, "dealData: dbFile not exists");
                dbFile.createNewFile();
            }
            if (!backupFile.exists()) {
                //Log.i(TAG, "dealData: backupFile not exists");
                backupFile.createNewFile();
            }
            if (isBackup) {
                fileCopy(dbFile, backupFile);
            } else {
                File tmpFile = new File(path, tmpFileName);
                if (!tmpFile.exists()) {
                    //Log.i(TAG, "dealData: backupFile not exists");
                    tmpFile.createNewFile();
                }
                fileCopy(dbFile, tmpFile);
                fileCopy(backupFile, dbFile);
                ComicUtil.initComicList(STATUS_ALL);
                tmpFile.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (!isBackup) {
                    File tmpFile = new File(path, tmpFileName);
                    File dbFile = context.getDatabasePath(dbFileName);
                    if (tmpFile.exists()) {
                        fileCopy(tmpFile, dbFile);
                        tmpFile.delete();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private static void fileCopy(File oFile, File toFile) {
        try (FileChannel inChannel = new FileInputStream(oFile).getChannel(); FileChannel outChannel = new FileOutputStream(toFile).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
