package com.qc.mycomic.util;

import java.io.*;

/**
 * @author LuQiChuang
 * @desc
 * @date 1/12/2021 11:05 AM
 * @ver 1.0
 */
public class FileUtil {

    public static final String MH_PATH = "D:\\Programming\\Files\\MH\\";

    public static String readFile(String fileName) {
        try {
            String filePath = MH_PATH + fileName;
            File file = getFile(filePath);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                builder.append(line).append("\n");
            }
            in.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFile(String html, String fileName) {
        try {
            String filePath = MH_PATH + fileName;
            File file = getFile(filePath);
            BufferedWriter in = new BufferedWriter(new FileWriter(file));
            in.write(html);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(String filePath){
        File file = new File(filePath);
        return file.exists();
    }

    private static File getFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

}
