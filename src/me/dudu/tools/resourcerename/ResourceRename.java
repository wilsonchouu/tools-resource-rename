package me.dudu.tools.resourcerename;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Author : zhouyx
 * Date   : 2017/8/14
 * Description :
 */
public class ResourceRename {

    // drawable
//    private static final String XHDPI = "drawable-xhdpi";
//    private static final String XXHDPI = "drawable-xxhdpi";

    // mipmap
    private static final String XHDPI = "mipmap-xhdpi";
    private static final String XXHDPI = "mipmap-xxhdpi";

    /**
     * 获取jar文件目录
     *
     * @return
     */
    public String getCurrentLocation() {
        String path = ResourceRename.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");// 转换处理中文及空格
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new File(path).getParent();
    }

    /**
     * 创建文件夹
     *
     * @param root
     */
    public void createResourceDir(String root) {
        File file = new File(root);
        String path = file.getAbsolutePath();

        File xhdpi = new File(path + File.separator + XHDPI);
        if (!xhdpi.exists()) {
            xhdpi.mkdir();
        }
        File xxhdpi = new File(path + File.separator + XXHDPI);
        if (!xxhdpi.exists()) {
            xxhdpi.mkdir();
        }
    }

    /**
     * 将文件重命名并移到相应目录
     *
     * @param root
     */
    public void moveFiles(String root) {
        File rootFile = new File(root);
        String path = rootFile.getAbsolutePath();

        File[] files = rootFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!file.isFile()) {//不是文件则进入下个循环
                continue;
            }
            if (!file.getName().endsWith(".png")) {//不是png文件则进入下个循环
                continue;
            }

            String originalName = path + File.separator + file.getName();
            String newName = file.getName();
            if (newName.contains("@3x")) {
                newName = path + File.separator + XXHDPI + File.separator + rename(newName);
            } else if (newName.contains("@2x")) {
                newName = path + File.separator + XHDPI + File.separator + rename(newName);
            }

            //复制文件进新目录
            try {
                FileInputStream input = new FileInputStream(originalName);
                FileOutputStream output = new FileOutputStream(newName);

                int in;
                while ((in = input.read()) != -1) {
                    output.write(in);
                }
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(newName + "\n");
        }
        System.out.println("文件转换完成\n");
    }

    /**
     * 重命名文件
     *
     * @param fileName
     * @return
     */
    private String rename(String fileName) {
        fileName = fileName.trim()//去前后空格
                .toLowerCase()//转换为小写
                .replaceAll("@3x|@2x|.png", "");//去@3x、@2x、文件后缀名

        // 如果文件名第一个字符为数字，则替换为小写字母
        char firstChar = fileName.charAt(0);
        if (isNumeric(String.valueOf(firstChar))) {
            // 97 - 48 = 49
            // a - 0 = 49
            fileName = fileName.replaceAll(String.valueOf(firstChar), String.valueOf((char) (48 + firstChar)));
        }

        // 替换非法字符为_
        for (int i = 0; i < fileName.length(); i++) {
            char c = fileName.charAt(i);
            if (isIllegal(String.valueOf(c))) {
                fileName = fileName.replace(String.valueOf(c), "_");
            }
        }
        return fileName + ".png";
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    private boolean isNumeric(String str) {
        String regex = "[0-9]";
        return Pattern.compile(regex).matcher(str).matches();
    }

    /**
     * 判断是否非法字符
     *
     * @param str
     * @return
     */
    private boolean isIllegal(String str) {
        String regex = "[a-z]|_";
        return !Pattern.compile(regex).matcher(str).matches();
    }

}
