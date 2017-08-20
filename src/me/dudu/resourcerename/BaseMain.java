package me.dudu.resourcerename;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Author : zhouyx
 * Date   : 2017/8/20
 * Description :
 */
public class BaseMain {

    /**
     * 获取jar文件目录
     *
     * @return
     */
    protected static String getAppLocation() {
        String path = BaseMain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            path = URLDecoder.decode(path, "UTF-8");// 转换处理中文及空格
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new File(path).getParent();
    }

}
