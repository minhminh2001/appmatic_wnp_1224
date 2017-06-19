package com.whitelabel.app.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

public class FileUtils {

    /**
     * 获取文件夹大小 **
     */
    public static long getDirSize(File f) throws Exception {
        long size = 0;
        File[] flist = f.listFiles();
        if (flist != null)
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getDirSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        return size;
    }

    /**
     * 转换文件大小单位(b/kb/mb/gb) **
     */
    public String formetFileSize(long fileS) {// 转换文件大小
        if (fileS == 0)
            return "0KB";
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "Bytes";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取文件个数 **
     */
    public static long getlistSize(File f) {// 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlistSize(flist[i]);
                size--;
            }
        }
        return size;
    }

    /**
     * 从 sdcard/linked-runner/file/filename 读取字符串内容
     */
    public static String readFromSdFile(String bucket, String filename) {
        String path = AppUtils.getMyCacheDir(bucket);
        File file = new File(path, filename);

        StringBuilder result = new StringBuilder();

        try {
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream bin = new BufferedInputStream(fin);
            byte[] buffer = new byte[1024];
            int hasRead;

            while ((hasRead = bin.read(buffer)) > -1) {
                result.append(new String(buffer, 0, hasRead));
            }

            bin.close();
            fin.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static String readFromSdFile(String filename) {
        return readFromSdFile("file", filename);
    }

    /**
     * 读取assest文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssest(Context context, String fileName) {
        String result = "";
        InputStream input = null;
        try {
            input = context.getAssets().open(fileName);
            int length = input.available();
            byte[] buffer = new byte[length];
            input.read(buffer);

            result = org.apache.http.util.EncodingUtils.getString(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 删除 文件夹 以及 目录下所有文件
     */
    public static void deleteFile(File file) {
        try {
            if (file.exists()) {
                if (file.isFile()) {

                    final File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
                    file.renameTo(to);
                    to.delete();

                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);// 递归
                    }
                }
                // file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹file中的文件
     *
     * @param filename
     * @return
     */
    public static boolean deleteSdFile(String filename) {
        if (!isSDCardExist())
            return false;
        String path = AppUtils.getMyCacheDir("file");
        File file = new File(path, filename);
        file.delete();
        return true;
    }

    /**
     * 删除文件夹bucket中的文件filename
     *
     * @param bucket
     * @param filename
     * @return
     */
    public static boolean deleteSdFile(String bucket, String filename) {
        if (!isSDCardExist())
            return false;
        String path = AppUtils.getMyCacheDir(bucket);
        File file = new File(path, filename);
        file.delete();
        return true;
    }

    public static boolean appendToSdFile(String string, String filename) {
        return appendToSdFile(string, "file", filename);
    }

    /**
     * 从文件尾部追加内容
     *
     * @param string
     * @param filename
     * @param bucket   根目录下的子目录
     * @return
     */
    public static boolean appendToSdFile(String string, String bucket, String filename) {

        String path = AppUtils.getMyCacheDir(bucket);
        File file = new File(path, filename);

        boolean success = false;
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            BufferedOutputStream bout = new BufferedOutputStream(fos);
            bout.write(string.getBytes());
            bout.flush();

            success = true;

            bout.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile     源文件File
     * @param destDir     目标目录File
     * @param newFileName 新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @SuppressWarnings("resource")
    public static long copyFile(File srcFile, File destDir, String newFileName, CopyFileListener listener) {
        long copySizes = 0;
        if (!srcFile.exists()) {
            if (listener != null)
                listener.exception("源文件不存在");
            copySizes = -1;
        } else if (!destDir.exists()) {
            if (listener != null)
                listener.exception("目标目录不存在");
            copySizes = -1;
        } else if (newFileName == null) {
            if (listener != null)
                listener.exception("文件名为null");
            copySizes = -1;
        } else {
            try {
                File dstFile = new File(destDir, newFileName);

                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(dstFile).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                fcin.close();
                fcout.close();
                copySizes = size;

                if (listener != null)
                    listener.success(dstFile.getPath());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return copySizes;
    }

    public static boolean isFileExist(String bucket, String filename) {
        String path = AppUtils.getMyCacheDir(bucket);
        File file = new File(path, filename);

        return file.exists();

    }

    public static boolean isSDCardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String readString(String path) {
        String result = "";
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String s = "";
            while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
                result = result + s + "\n";
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeString(String filePath, String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();// 不存在则创建
            }
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(content);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File createFile(Context context, String fileName, String mediaType) {
        if (isSDCardExist()) {
            return new File(context.getExternalFilesDir(mediaType), fileName);
        } else {
            return new File(context.getFilesDir(), fileName);
        }
    }

    public interface CopyFileListener {
        void exception(String msg);

        void success(String path);
    }
}
