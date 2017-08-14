package me.dudu.tools.resourcerename;

/**
 * Author : zhouyx
 * Date   : 2017/8/14
 * Description :
 */
public class Main {

    public static void main(String[] args) {
        ResourceRename resourceRename = new ResourceRename();
        String currentLocation = resourceRename.getCurrentLocation();
        System.out.println("当前目录：" + currentLocation + "\n");

        System.out.println("创建文件夹\n");
        resourceRename.createResourceDir(currentLocation);

        System.out.println("开始处理文件\n");
        resourceRename.moveFiles(currentLocation);
    }

}
