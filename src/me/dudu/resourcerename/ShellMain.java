package me.dudu.resourcerename;


import me.dudu.resourcerename.tool.ResourceRename;

/**
 * Author : zhouyx
 * Date   : 2017/8/14
 * Description :
 */
public class ShellMain extends BaseMain {

    public static void main(String[] args) {
        String currentLocation = getAppLocation();
        System.out.println("当前目录：" + currentLocation + "\n");

        new ResourceRename().setResourceType(ResourceRename.MIPMAP_TYPE)
                .setRoot(currentLocation)
                .setOnProgressListener(new ResourceRename.OnProgressListener() {
                    @Override
                    public void onStart(int total) {
                        System.out.println("开始处理 : " + total);
                    }

                    @Override
                    public void onProgress(String originalPath, String newPath, int progress, int total) {
                        System.out.print(" originalPath : " + originalPath);
                        System.out.print(" newPath : " + newPath);
                        System.out.print(" progress : " + progress);
                        System.out.println();
                    }

                    @Override
                    public void onFinish() {
                        System.out.println("处理完成");
                    }
                }).start();
    }

}
