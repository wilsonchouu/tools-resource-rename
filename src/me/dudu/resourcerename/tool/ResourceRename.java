package me.dudu.resourcerename.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author : zhouyx
 * Date   : 2017/8/14
 * Description :
 */
public class ResourceRename {

    public static final int DRAWABLE_TYPE = 10000;
    public static final int MIPMAP_TYPE = 20000;

    private String xhdpiDir = "";
    private String xxhdpiDir = "";

    private int resourceType;
    private String root;
    private OnProgressListener onProgressListener;

    public ResourceRename setResourceType(int resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public ResourceRename setRoot(String root) {
        this.root = root;
        return this;
    }

    public ResourceRename setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
        return this;
    }

    /**
     * 开始分类
     */
    public void start() {
        if (resourceType == DRAWABLE_TYPE) {
            this.xhdpiDir = "drawable-xhdpi";
            this.xxhdpiDir = "drawable-xxhdpi";
        } else if (resourceType == MIPMAP_TYPE) {
            this.xhdpiDir = "mipmap-xhdpi";
            this.xxhdpiDir = "mipmap-xxhdpi";
        } else {
            throw new IllegalArgumentException("Unknown resource type");
        }
        if (root == null || "".equals(root)) {
            throw new IllegalArgumentException("location root is null or empty");
        }
        createResourceDir(root);
        arrangeFiles(root);
    }

    /**
     * 创建文件夹
     *
     * @param root
     */
    private void createResourceDir(String root) {
        File file = new File(root);
        String path = file.getAbsolutePath();

        File xhdpi = new File(path + File.separator + xhdpiDir);
        if (!xhdpi.exists()) {
            xhdpi.mkdir();
        }
        File xxhdpi = new File(path + File.separator + xxhdpiDir);
        if (!xxhdpi.exists()) {
            xxhdpi.mkdir();
        }
    }

    /**
     * 将文件重命名并移到相应目录
     *
     * @param root
     */
    private void arrangeFiles(String root) {
        File rootFile = new File(root);
        String path = rootFile.getAbsolutePath();

        File[] fileArr = rootFile.listFiles();
        if (fileArr == null) {
            return;
        }
        List<File> files = new ArrayList<>();
        //过滤文件夹
        for (File file : fileArr) {
            if (!file.isFile()) {//不是文件则进入下个循环
                continue;
            }
            if (!file.getName().endsWith(".jpg")
                    && !file.getName().endsWith(".jpeg")
                    && !file.getName().endsWith(".png")) {//格式检查
                continue;
            }
            files.add(file);
        }
        int totalSize = files.size();
        if (totalSize == 0) {
            return;
        }
        if (onProgressListener != null) {
            onProgressListener.onStart(totalSize);
        }
        try {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                String suffix = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
                String originalName = path + File.separator + file.getName();
                String newName = file.getName();
                if (newName.contains("@3x")) {
                    newName = path + File.separator + xxhdpiDir + File.separator + rename(newName, suffix);
                } else if (newName.contains("@2x")) {
                    newName = path + File.separator + xhdpiDir + File.separator + rename(newName, suffix);
                }

                //复制文件进新目录
                FileInputStream input = new FileInputStream(originalName);
                FileOutputStream output = new FileOutputStream(newName);
                int in;
                while ((in = input.read()) != -1) {
                    output.write(in);
                }
                input.close();
                output.close();
                if (onProgressListener != null) {
                    onProgressListener.onProgress(originalName, newName, i + 1, totalSize);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (onProgressListener != null) {
            onProgressListener.onFinish();
        }
    }

    /**
     * 重命名文件
     *
     * @param fileName
     * @return
     */
    private String rename(String fileName, String suffix) {
        fileName = fileName.trim()//去前后空格
                .toLowerCase()//转换为小写
                .replaceAll("@3x|@2x|" + suffix, "");//去@3x、@2x、文件后缀名

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
        return fileName + suffix;
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
        String regex = "[a-z]|[0-9]|_";
        return !Pattern.compile(regex).matcher(str).matches();
    }

    public interface OnProgressListener {
        void onStart(int total);

        void onProgress(String originalPath, String newPath, int progress, int total);

        void onFinish();
    }

}
