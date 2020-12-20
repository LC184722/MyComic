package com.qc.mycomic.setting;

/**
 * @author LuQiChuang
 * @desc 设置工厂
 * @date 2020/8/15 11:57
 * @ver 1.0
 */
public class SettingFactory {

    public static final int SETTING_DEFAULT_SOURCE = 0;
    public static final int SETTING_PRELOAD_NUM = 1;
    public static final int SETTING_COMPRESS_IMAGE = 2;

    private DSSetting dsSetting = new DSSetting();
    private PNSetting pnSetting = new PNSetting();
    private CISetting ciSetting = new CISetting();

    private static SettingFactory factory = new SettingFactory();

    public static SettingFactory getInstance() {
        return factory;
    }

    public Setting getSetting(int which) {
        if (which == SETTING_DEFAULT_SOURCE) {
            return dsSetting;
        } else if (which == SETTING_PRELOAD_NUM) {
            return pnSetting;
        } else if (which == SETTING_COMPRESS_IMAGE) {
            return ciSetting;
        } else {
            return pnSetting;
        }
    }

}
