package com.duoduovv.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author : liujun
 * @date : 2019-11-26 10:05
 * @description:
 */
public class FileUtils {
    public static void is2File(InputStream is, String filepath, Callback cb) {
        try {
            File file = new File(filepath);
            // 输出的文件流
            OutputStream os = null;
            try {
                if (file.exists()) file.delete();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                //已下载之数据
                long downloadSize = 0;
                os = new FileOutputStream(filepath);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                    cb.doSchedule(downloadSize += len);
                }
                cb.doSuss(filepath);
            } finally {
                // 完毕，关闭所有链接
                if (null != os) os.close();
                if (null != is) is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            cb.err(e.getMessage());
        }
    }

    public interface Callback {
        void doSuss(String filePath);

        void doSchedule(long downloadSize);

        void err(String str);

    }
}
