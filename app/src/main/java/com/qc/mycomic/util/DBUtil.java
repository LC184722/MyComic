package com.qc.mycomic.util;

import android.content.Context;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.ui.activity.MainActivity;
import com.qc.common.util.ImgUtil;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.luqichuang.common.util.DateUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;

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

    private static void init() {
        try {
            LitePalApplication.getContext();
        } catch (Exception e) {
            e.printStackTrace();
            LitePal.initialize(MainActivity.getInstance());
        }
    }

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
            init();
            if (comic.getTitle() == null) {
                comic.setTitle(comic.getComicInfo().getTitle());
                comic.setSourceId(comic.getComicInfo().getSourceId());
                comic.setStatus(STATUS_HIS);
            }
            comic.saveOrUpdate("title = ?", comic.getTitle());
            //Log.i(TAG, "saveComicData: comic --> " + comic.getTitle());
        }
    }

    private static void saveComicInfoData(ComicInfo comicInfo) {
        if (comicInfo != null) {
            init();
            comicInfo.saveOrUpdate("title = ? and sourceId = ?", comicInfo.getTitle(), String.valueOf(comicInfo.getSourceId()));
            //Log.i(TAG, "saveComicInfoData: comicInfo --> " + comicInfo.getTitle() + " " + SourceUtil.getSOURCE_NAME(comicInfo.getSourceId()));
        }
    }

    /**
     * 删除漫画
     *
     * @param comic 漫画
     * @return void
     */
    public static void deleteData(Comic comic) {
        if (comic != null) {
            init();
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
        init();
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
                    ComicHelper.addComicInfo(comic, info);
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
    private static List<ComicInfo> findComicInfoListByTitle(String title) {
        return LitePal.where("title = ?", title).find(ComicInfo.class);
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        init();
        return LitePal.findAll(clazz);
    }

    public static void autoBackup(Context context) {
        if (!existAuto()) {
            backupData(context, getAutoName());
        }
        deleteAuto();
    }

    public static String getAutoName() {
        return AppConstant.AUTO_SAVE_PATH + "/自动备份#" + DateUtil.formatAutoBackup(new Date());
    }

    public static boolean existAuto() {
        return new File(getAutoName()).exists();
    }

    public static boolean deleteAuto() {
        try {
            File file = new File(AppConstant.AUTO_SAVE_PATH);
            File[] files = file.listFiles();
            while (files.length > 5) {
                int old = 0;
                long oldVal = files[0].lastModified();
                for (int i = 1; i < files.length; i++) {
                    File f = files[i];
                    if (f.lastModified() < oldVal) {
                        old = i;
                        oldVal = f.lastModified();
                    }
                }
                files[old].delete();
                files = file.listFiles();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static final String SAVE_PATH_NAME = AppConstant.APP_PATH + "/backup_comic.db";

    public static boolean backupData(Context context) {
        return dealData(context, true, SAVE_PATH_NAME);
    }

    public static boolean backupData(Context context, String pathName) {
        return dealData(context, true, pathName);
    }

    public static boolean restoreData(Context context) {
        return dealData(context, false, SAVE_PATH_NAME);
    }

    public static boolean restoreData(Context context, String pathName) {
        return dealData(context, false, pathName);
    }

    private static boolean dealData(Context context, boolean isBackup, String pathName) {
        String dbFileName = "comic.db";
        String tmpFileName = "tmp.db";
        String path = AppConstant.APP_PATH;
        try {
            File dbFile = context.getDatabasePath(dbFileName);
            File backupFile = new File(pathName);
            if (!backupFile.getParentFile().exists()) {
                backupFile.getParentFile().mkdirs();
            }
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
