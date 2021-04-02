package top.luqichuang.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author LuQiChuang
 * @desc 日期工具
 * @date 2020/8/12 15:25
 * @ver 1.0
 */
public class DateUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private static SimpleDateFormat backupFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static String format(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }

    public static String formatAutoBackup(Date date) {
        return date != null ? backupFormat.format(date) : null;
    }

}
