package tool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author : zhouyx
 * Date   : 2017/8/14
 * Description :
 */
public class RenameHandler {

    private String[] dirs = new String[]{"drawable-xhdpi", "drawable-xxhdpi", "mipmap-xhdpi", "mipmap-xxhdpi"};
    private String root;
    private OnProgressListener onProgressListener;

    public RenameHandler(String root) {
        this.root = root;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public void start() {
        createResourceDir(root);
        arrange(root);
    }

    /**
     * 创建drawable、mipmap文件夹
     */
    private void createResourceDir(String root) {
        String path = new File(root).getAbsolutePath();
        for (String dir : dirs) {
            File file = new File(path + File.separator + dir);
            if (!file.exists()) {
                file.mkdir();
            }
        }
    }

    /**
     * 分类文件
     */
    private void arrange(String root) {
        String path = new File(root).getAbsolutePath();

        File[] files = new File(root).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jpg")
                        || pathname.getName().endsWith(".jpeg")
                        || pathname.getName().endsWith(".png");
            }
        });
        if (files == null || files.length == 0) {
            return;
        }
        int totalSize = files.length;
        if (onProgressListener != null) {
            onProgressListener.onStart(totalSize);
        }
        try {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String suffix = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
                File drawable = null, mipmap = null;
                if (file.getName().contains("@3x")) {
                    drawable = new File(path + File.separator + dirs[1] + File.separator + rename(file.getName(), suffix));
                    mipmap = new File(path + File.separator + dirs[3] + File.separator + rename(file.getName(), suffix));
                } else if (file.getName().contains("@2x")) {
                    drawable = new File(path + File.separator + dirs[0] + File.separator + rename(file.getName(), suffix));
                    mipmap = new File(path + File.separator + dirs[2] + File.separator + rename(file.getName(), suffix));
                }
                if (drawable == null) {
                    continue;
                }

                copy(file, drawable);
                copy(file, mipmap);

                if (onProgressListener != null) {
                    onProgressListener.onProgress(i + 1, totalSize, file.getAbsolutePath(), drawable.getAbsolutePath(), mipmap.getAbsolutePath());
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
     * 复制文件
     *
     * @param original
     * @param target
     * @throws IOException
     */
    private void copy(File original, File target) throws IOException {
        FileInputStream input = new FileInputStream(original);
        FileOutputStream output = new FileOutputStream(target);
        int in;
        while ((in = input.read()) != -1) {
            output.write(in);
        }
        input.close();
        output.close();
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

        void onProgress(int progress, int total, String original, String drawable, String mipmap);

        void onFinish();
    }

}
