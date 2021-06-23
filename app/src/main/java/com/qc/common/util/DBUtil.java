package com.qc.common.util;

import android.content.Context;

import com.qc.common.constant.AppConstant;
import com.qc.common.constant.Constant;
import com.qc.common.constant.TmpData;
import com.qc.common.ui.activity.MainActivity;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.luqichuang.common.model.Entity;
import top.luqichuang.common.model.EntityInfo;
import top.luqichuang.common.model.Source;
import top.luqichuang.common.util.DateUtil;
import top.luqichuang.common.util.SourceUtil;
import top.luqichuang.mycomic.model.Comic;
import top.luqichuang.mycomic.model.ComicInfo;
import top.luqichuang.mynovel.model.Novel;
import top.luqichuang.mynovel.model.NovelInfo;
import top.luqichuang.myvideo.model.Video;
import top.luqichuang.myvideo.model.VideoInfo;

/**
 * @author LuQiChuang
 * @desc
 * @date 2021/6/9 19:07
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

    private static final ExecutorService POOL = Executors.newFixedThreadPool(3);

    private static void init() {
        try {
            LitePalApplication.getContext();
        } catch (Exception e) {
            e.printStackTrace();
            LitePal.initialize(MainActivity.getInstance());
        }
    }

    public static void save(Entity entity) {
        save(entity, SAVE_CUR);
    }

    public static void save(Entity entity, int mode) {
        if (entity != null) {
            if (mode == SAVE_ONLY) {
                saveEntityData(entity);
            } else if (mode == SAVE_CUR) {
                saveEntityData(entity);
                saveInfoData(entity.getInfo());
            } else {
                saveEntityData(entity);
                for (EntityInfo info : entity.getInfoList()) {
                    saveInfoData(info);
                }
            }
        }
    }

    public static void saveEntityData(Entity entity) {
        if (entity != null) {
            init();
            POOL.execute(() -> {
                if (TmpData.contentCode == AppConstant.COMIC_CODE || TmpData.contentCode == AppConstant.VIDEO_CODE) {
                    entity.saveOrUpdate("title = ? and sourceId = ?", entity.getTitle(), String.valueOf(entity.getSourceId()));
                } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                    entity.saveOrUpdate("title = ? and nSourceId = ? and author = ?", entity.getTitle(), String.valueOf(entity.getSourceId()), entity.getInfo().getAuthor());
                }
            });
        }
    }

    public static void saveInfoData(EntityInfo info) {
        if (info != null) {
            init();
            POOL.execute(() -> {
                if (TmpData.contentCode == AppConstant.COMIC_CODE || TmpData.contentCode == AppConstant.VIDEO_CODE) {
                    info.saveOrUpdate("title = ? and sourceId = ?", info.getTitle(), String.valueOf(info.getSourceId()));
                } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                    info.saveOrUpdate("title = ? and nSourceId = ? and author = ?", info.getTitle(), String.valueOf(info.getSourceId()), info.getAuthor());
                }
            });
        }
    }

    public static void deleteData(Entity entity) {
        if (entity != null) {
            init();
            POOL.execute(() -> {
                entity.delete();
                for (EntityInfo info : entity.getInfoList()) {
                    File file = new File(ImgUtil.getLocalImgUrl(info.getId()));
                    if (file.exists()) {
                        file.delete();
                    }
                }
            });
        }
    }

    public static List<? extends Entity> findListByStatus(int status) {
        String order = "priority DESC, date DESC";
        init();
        List<? extends Entity> list;
        if (TmpData.contentCode == AppConstant.COMIC_CODE) {
            if (status == Constant.STATUS_ALL) {
                list = LitePal.order(order).find(Comic.class);
            } else {
                list = LitePal.where("status = ?", String.valueOf(status)).order(order).find(Comic.class);
            }
        } else if (TmpData.contentCode == AppConstant.READER_CODE) {
            if (status == Constant.STATUS_ALL) {
                list = LitePal.order(order).find(Novel.class);
            } else {
                list = LitePal.where("status = ?", String.valueOf(status)).order(order).find(Novel.class);
            }
        } else {
            if (status == Constant.STATUS_ALL) {
                list = LitePal.order(order).find(Video.class);
            } else {
                list = LitePal.where("status = ?", String.valueOf(status)).order(order).find(Video.class);
            }
        }
        List<Entity> dList = new ArrayList<>();
        for (Entity entity : list) {
            List<? extends EntityInfo> infoList;
            if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                infoList = LitePal.where("title = ?", entity.getTitle()).find(ComicInfo.class);
            } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                infoList = LitePal.where("title = ?", entity.getTitle()).find(NovelInfo.class);
            } else {
                infoList = LitePal.where("title = ?", entity.getTitle()).find(VideoInfo.class);
            }
            for (EntityInfo info : infoList) {
                Source source;
                if (TmpData.contentCode == AppConstant.COMIC_CODE) {
                    source = SourceUtil.getSource(info.getSourceId());
                } else if (TmpData.contentCode == AppConstant.READER_CODE) {
                    source = SourceUtil.getNSource(info.getSourceId());
                } else {
                    source = SourceUtil.getVSource(info.getSourceId());
                }
                if (source != null && source.isValid()) {
                    EntityHelper.addInfo(entity, info);
                    if (entity.getSourceId() == info.getSourceId()) {
                        entity.setInfo(info);
                    }
                    //更改detailUrl
                    String url = info.getDetailUrl();
                    String index = source.getIndex();
                    if (!url.startsWith(index)) {
                        String tmp = url.substring(url.indexOf('/', url.indexOf('.')));
                        url = index + tmp;
                        info.setDetailUrl(url);
                        saveInfoData(info);
                    }
                    //end
                }
            }
            if (entity.getInfo() == null) {
                if (entity.getInfoList().isEmpty()) {
                    dList.add(entity);
                } else {
                    entity.setInfo(entity.getInfoList().get(0));
                    entity.setSourceId(entity.getInfoList().get(0).getSourceId());
                }
            }
        }
        if (dList.size() > 0) {
            list.removeAll(dList);
        }
        return list;
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
                EntityUtil.initEntityList(EntityUtil.STATUS_ALL);
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
