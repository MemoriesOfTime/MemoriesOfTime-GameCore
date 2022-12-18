package cn.lanink.gamecore.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("unused")
public class ZipUtils {

    /**
     * 解压
     *
     * @param zipFilePath 带解压文件
     * @param desDirectory 解压到的目录
     * @throws Exception 异常
     */
    public static void unzip(String zipFilePath, String desDirectory) throws Exception {
        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }
        ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)));
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
            if (zipEntry.isDirectory()) {
                mkdir(new File(unzipFilePath));
            } else {
                File file = new File(unzipFilePath);
                mkdir(file.getParentFile());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(unzipFilePath)));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();
    }

}
