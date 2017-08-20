package me.dudu.resourcerename;


import me.dudu.resourcerename.swing.AppContainer;
import me.dudu.resourcerename.tool.ResourceRename;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Author : zhouyx
 * Date   : 2017/8/20
 * Description :
 */
public class SwingMain extends BaseMain {

    public static void main(String[] args) {
        String currentLocation = getAppLocation();
        System.out.println("当前目录：" + currentLocation + "\n");

        initUIManager();

        JFrame frame = createFrame();
        final AppContainer container = new AppContainer(frame, getAppLocation());
        container.setOnButtonClickListener(new AppContainer.OnButtonClickListener() {
            @Override
            public void onDrawableClick(String location) {
                new ResourceRename().setResourceType(ResourceRename.DRAWABLE_TYPE)
                        .setRoot(location)
                        .setOnProgressListener(new ResourceRename.OnProgressListener() {
                            @Override
                            public void onStart(int total) {
                                container.initDetailMessage();
                                container.appendDetailMessage("开始处理 : 共" + total + "个文件");
                            }

                            @Override
                            public void onProgress(String originalPath, String newPath, int progress, int total) {
                                container.appendDetailMessage(progress + "/" + total + " >>>>> " + newPath);
                            }

                            @Override
                            public void onFinish() {
                                container.appendDetailMessage("处理完成");
                            }
                        }).start();
            }

            @Override
            public void onMipmapClick(String location) {
                new ResourceRename().setResourceType(ResourceRename.MIPMAP_TYPE)
                        .setRoot(location)
                        .setOnProgressListener(new ResourceRename.OnProgressListener() {
                            @Override
                            public void onStart(int total) {
                                container.initDetailMessage();
                                container.appendDetailMessage("开始处理 : 共" + total + "个文件");
                            }

                            @Override
                            public void onProgress(String originalPath, String newPath, int progress, int total) {
                                container.appendDetailMessage(progress + "/" + total + " >>>>> " + newPath);
                            }

                            @Override
                            public void onFinish() {
                                container.appendDetailMessage("处理完成");
                            }
                        }).start();
            }
        });
        frame.setContentPane(container.getContainerPanel());
        frame.setVisible(true);
    }

    private static void initUIManager() {
        try {
            // 将LookAndFeel设置成Windows样式
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        UIManager.put("Button.select", new ColorUIResource(new Color(80, 85, 85)));
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("ResourceRename");
        frame.setSize(new Dimension(600, 400));
        frame.setLocationRelativeTo(null);
        try {
            String logo = "./res/logo.png";
            BufferedImage image = ImageIO.read(new File(logo));
            frame.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frame;
    }

}
