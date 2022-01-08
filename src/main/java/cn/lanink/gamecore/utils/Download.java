package cn.lanink.gamecore.utils;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @author iGxnon
 * 多线程下载
 */
public class Download {

    // 每个任务下载 128 kb数据
    private static final int THRESHOLD = 128 * 1024;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 下载
     * @param strUrl 目标url
     * @param saveFile 保存到文件
     * @param callback 下载完的回调
     */
    public static void download(String strUrl, File saveFile, Consumer<File> callback) {
        if (saveFile.exists()) {
            return;
        }
        executor.submit(() -> {
            try {
                URL url = new URL(strUrl);
                HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
                connection.setReadTimeout(5000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setRequestProperty("Accept", "*/*");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

                long len = connection.getContentLength();
                if ("chunked".equals(connection.getHeaderField("Transfer-Encoding"))) { // chunked transfer 采用单线程下载
                    RandomAccessFile out = new RandomAccessFile(saveFile, "rw");
                    out.seek(0);
                    byte[] b = new byte[1024];
                    InputStream in = connection.getInputStream();
                    int read = 0;
                    while ((read = in.read(b)) >= 0) {
                        out.write(b, 0, read);
                    }
                    in.close();
                    out.close();
                    if (callback == null) return;
                    callback.accept(saveFile);
                    return;
                }
                ForkJoinPool pool = new ForkJoinPool();
                pool.submit(new DownloadTask(strUrl,0, len, saveFile));
                pool.shutdown();
                // 同步
                while (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                }
                if (callback == null) return;
                callback.accept(saveFile);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    static class DownloadTask extends RecursiveAction {

        private final String strUrl;
        private final File file;
        private final long start;
        private final long end;

        public DownloadTask(@NonNull String strUrl, long start, long end, File file) {
            this.strUrl = strUrl;
            this.start = start;
            this.end = end;
            this.file = file;
        }

        @SneakyThrows
        @Override
        protected void compute() {
            if (end - start < THRESHOLD) {
                HttpURLConnection connection = getConnection();
                connection.setRequestProperty("Range", "bytes=" + start + "-" + end);

                RandomAccessFile out = new RandomAccessFile(file, "rw");
                out.seek(start);
                InputStream in = connection.getInputStream();
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = in.read(b)) >= 0) {
                    out.write(b, 0, len);
                }
                in.close();
                out.close();
            }else {
                long mid = (start + end) / 2;
                DownloadTask left = new DownloadTask(strUrl, start, mid, file);
                DownloadTask right = new DownloadTask(strUrl, mid, end, file);
                left.fork();
                right.fork();
            }
        }

        public HttpURLConnection getConnection() throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL(strUrl).openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            return connection;
        }
    }

}

