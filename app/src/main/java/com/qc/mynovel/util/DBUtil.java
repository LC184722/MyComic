package com.qc.mynovel.util;

import android.content.Context;
import android.util.Log;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.util.ImgUtil;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.luqichuang.common.util.NSourceUtil;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;

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
     * 保存小说信息，默认保存所有小说信息
     *
     * @param novel novel
     * @return void
     */
    public static void saveNovel(Novel novel) {
        saveNovel(novel, SAVE_ALL);
    }

    /**
     * 根据mode，保存小说信息
     *
     * @param novel novel
     * @param mode  DBUtil.SAVE_* , only - 保存小说信息，cur - 保存当前源信息，all - 保存所有源信息
     * @return void
     */
    public static void saveNovel(Novel novel, int mode) {
        if (novel != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mode == SAVE_ONLY) {
                        saveNovelData(novel);
                    } else if (mode == SAVE_CUR) {
                        saveNovelData(novel);
                        saveNovelInfoData(novel.getNovelInfo());
                    } else {
                        saveNovelData(novel);
                        for (NovelInfo novelInfo : novel.getNovelInfoList()) {
                            saveNovelInfoData(novelInfo);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 保存小说源信息
     *
     * @param novelInfo novelInfo
     * @return void
     */
    public static void saveNovelInfo(NovelInfo novelInfo) {
        if (novelInfo != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveNovelInfoData(novelInfo);
                }
            }).start();
        }
    }

    private static void saveNovelData(Novel novel) {
        if (novel != null) {
            if (novel.getTitle() == null) {
                novel.setTitle(novel.getNovelInfo().getTitle());
                novel.setNSourceId(novel.getNovelInfo().getNSourceId());
                novel.setDate(new Date());
                novel.setStatus(STATUS_HIS);
            }
            novel.saveOrUpdate("title = ?", novel.getTitle());
            //Log.i(TAG, "saveNovelData: novel --> " + novel.getTitle());
        }
    }

    private static void saveNovelInfoData(NovelInfo novelInfo) {
        if (novelInfo != null) {
            novelInfo.saveOrUpdate("title = ? and nSourceId = ?", novelInfo.getTitle(), String.valueOf(novelInfo.getNSourceId()));
        }
    }

//    /**
//     * 保存该小说信息及所有小说源信息
//     *
//     * @param novel 保存小说
//     * @return void
//     */
//    public static void saveData(Novel novel) {
//        saveData(novel, true);
//    }
//
//    /**
//     * 保存小说信息
//     *
//     * @param novel    当前小说
//     * @param needInfo 是否保存所有小说源信息
//     * @return void
//     */
//    public static void saveData(Novel novel, boolean needInfo) {
//        if (novel != null) {
//            new Thread(() -> {
//                novel.saveOrUpdate("title = ?", novel.getTitle());
//                //Log.i(TAG, "saveNovel: " + novel.getTitle() + " p = " + novel.getPriority());
//                if (!novel.getNovelInfoList().isEmpty() && needInfo) {
//                    for (NovelInfo info : novel.getNovelInfoList()) {
//                        DBUtil.saveData(info);
//                    }
//                }
//            }).start();
//        }
//    }
//
//    /**
//     * 保存小说源信息
//     *
//     * @param novelInfo 当前小说源
//     * @return void
//     */
//    public static void saveData(NovelInfo novelInfo) {
//        if (novelInfo != null) {
//            new Thread(() -> novelInfo.saveOrUpdate("title = ? and sourceId = ?", novelInfo.getTitle(), String.valueOf(novelInfo.getNSourceId()))).start();
//        }
//    }

    /**
     * 删除小说
     *
     * @param novel 小说
     * @return void
     */
    public static void deleteData(Novel novel) {
        if (novel != null) {
            new Thread(() -> {
                novel.delete();
                for (NovelInfo novelInfo : novel.getNovelInfoList()) {
                    File file = new File(ImgUtil.getLocalImgUrl(novelInfo.getId()));
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }).start();
//            new Thread(novel::delete).start();
        }
    }

    /**
     * 根据status查询小说
     *
     * @param status 小说状态,0 - 收藏，1 - 历史，2 - 所有
     * @return List<Novel>
     */
    public static List<Novel> findNovelListByStatus(int status) {
        List<Novel> list;
        String order = "priority DESC, date DESC";
        if (status == Constant.STATUS_ALL) {
            list = LitePal.order(order).find(Novel.class);
        } else {
            list = LitePal.where("status = ?", String.valueOf(status)).order(order).find(Novel.class);
        }
        List<Novel> dList = new ArrayList<>();
        for (Novel novel : list) {
            List<NovelInfo> infoList = findNovelInfoListByTitle(novel.getTitle());
            for (NovelInfo info : infoList) {
                if (NSourceUtil.getNSource(info.getNSourceId()).isValid()) {
                    NovelHelper.addNovelInfo(novel, info);
                    if (novel.getNSourceId() == info.getNSourceId()) {
                        novel.setNovelInfo(info);
                    }
                    //更改detailUrl
                    String url = info.getDetailUrl();
                    String index = NSourceUtil.getNSource(info.getNSourceId()).getIndex();
                    if (!url.startsWith(index)) {
                        String tmp = url.substring(url.indexOf('/', url.indexOf('.')));
                        url = index + tmp;
                        info.setDetailUrl(url);
                        saveNovelInfo(info);
                    }
                    //end
                }
            }
            if (novel.getNovelInfo() == null) {
                if (novel.getNovelInfoList().isEmpty()) {
                    dList.add(novel);
                } else {
                    novel.setNovelInfo(novel.getNovelInfoList().get(0));
                }
            }
        }
        if (dList.size() > 0) {
            list.removeAll(dList);
        }
        return list;
    }

    /**
     * 根据小说标题查找信息表里所有信息
     *
     * @param title 小说标题
     * @return List<NovelInfo>
     */
    public static List<NovelInfo> findNovelInfoListByTitle(String title) {
        return LitePal.where("title = ?", title).find(NovelInfo.class);
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
        String dbFileName = "novel.db";
        String backupFileName = "backup_novel.db";
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
                NovelUtil.initNovelList(STATUS_ALL);
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
