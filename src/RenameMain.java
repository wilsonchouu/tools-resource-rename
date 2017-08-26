import swing.UIContainer;
import tool.RenameHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Author : zhouyx
 * Date   : 2017/8/26
 * Description :
 */
public class RenameMain {

    public static void main(String[] args) {
//        runShell();
        runSwing();
    }

    /**
     * 可直接运行版本
     */
    private static void runShell() {
        RenameHandler handler = new RenameHandler(getRuntimeLocation());
        handler.setOnProgressListener(new RenameHandler.OnProgressListener() {
            @Override
            public void onStart(int total) {
                System.out.println("共处理文件总数 : " + total);
            }

            @Override
            public void onProgress(int progress, int total, String original, String drawable, String mipmap) {
                System.out.println(progress + "/" + total + " : " + original + " --> " + drawable + " , " + mipmap);
            }

            @Override
            public void onFinish() {
                System.out.println("处理完成");
            }
        });
        handler.start();
    }

    /**
     * 带GUI版本
     */
    private static void runSwing() {
        UIContainer container = new UIContainer();
        container.setLocation(getRuntimeLocation());
        container.setOnClickListener(new UIContainer.OnClickListener() {
            @Override
            public void onClick(UIContainer uiContainer, String location) {
                RenameHandler handler = new RenameHandler(location);
                handler.setOnProgressListener(new RenameHandler.OnProgressListener() {
                    @Override
                    public void onStart(int total) {
                        uiContainer.setMessage("开始处理文件" + total + "个");
                    }

                    @Override
                    public void onProgress(int progress, int total, String original, String drawable, String mipmap) {

                    }

                    @Override
                    public void onFinish() {
                        uiContainer.setMessage("处理完成!!!");
                    }
                });
                handler.start();
            }
        });
    }

    /**
     * 获取运行文件目录
     */
    private static String getRuntimeLocation() {
        String path = RenameMain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = URLDecoder.decode(path, "UTF-8");// 转换处理中文及空格
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new File(path).getParent();
    }

}
