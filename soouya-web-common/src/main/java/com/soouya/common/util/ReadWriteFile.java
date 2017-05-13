package com.soouya.common.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/** */

/**
 *
 * 功能描述：创建TXT文件并进行读、写、修改操作
 *
 * @author <a href="mailto:zhanghhui@126.com">KenZhang</a>
 * @version 1.0 Creation date: 2007-12-18 - 下午06:48:45
 */
public class ReadWriteFile {
    // public static BufferedReader bufread;
    // 指定文件路径和名称
    // private static String path = "count.dat";
    // private static File filename = new File(path);
    // private static String readStr ="";

    /** */
    /**
     * 创建文本文件.
     *
     * @throws IOException
     *
     */
    public static void creatTxtFile(File filename) throws IOException {
        if (!filename.exists()) {
            filename.createNewFile();
            System.err.println(filename + "已创建！");
        }
    }

    /** */
    /**
     * 读取文本文件.
     *
     */
    public static String readTxtFile(String filename) {
        String read;
        FileReader fileread;
        String readStr = "";
        BufferedReader bufread = null;
        try {
            fileread = new FileReader(new File(filename));
            bufread = new BufferedReader(fileread);
            try {
                while ((read = bufread.readLine()) != null) {
                    readStr = readStr + read + "\r\n";
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            bufread.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println("文件内容是:"+ "\r\n" + readStr);
        return readStr;
    }

    /** */
    /**
     * 写文件.
     *
     */
    public static void writeTxtFile(String newStr, File filename) throws IOException {
        String filein = newStr;
        // String readStr
        // =ReadWriteFile.readTxtFile(filename.getAbsolutePath());//先读取原有文件内容，然后进行写入操作
        // String filein = readStr + newStr;

        RandomAccessFile mm = null;
        try {
            mm = new RandomAccessFile(filename, "rw");
            mm.write(filein.getBytes("utf-8"));
        } catch (IOException e1) {
            // TODO 自动生成 catch 块
            e1.printStackTrace();
        } finally {
            if (mm != null) {
                try {
                    mm.close();
                } catch (IOException e2) {
                    // TODO 自动生成 catch 块
                    e2.printStackTrace();
                }
            }
        }
    }

    /** */
    /**
     * 将文件中指定内容的第一行替换为其它内容.
     *
     * @param oldStr
     *            查找内容
     * @param replaceStr
     *            替换内容
     */
    @SuppressWarnings("unused")
    public static void replaceTxtByStr(String oldStr, String replaceStr, String path) {
        String temp = "";
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该行前面的内容
            for (; (temp = br.readLine()) != null && !temp.equals(oldStr);) {
                buf = buf.append(temp);
                buf = buf.append(System.getProperty("line.separator"));
            }

            // 将内容插入
            buf = buf.append(replaceStr);

            // 保存该行后面的内容
            while ((temp = br.readLine()) != null) {
                buf = buf.append(System.getProperty("line.separator"));
                buf = buf.append(temp);
            }

            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getFiles(String filePath) {
        ArrayList<String> filelist = new ArrayList<String>();
        File root = new File(filePath);
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                filelist.addAll(getFiles(file.getAbsolutePath()));
                filelist.add(file.getAbsolutePath());
            } else {
                filelist.add(file.getAbsolutePath());
            }
        }
        return filelist;
    }

    @SuppressWarnings("unused")
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    // System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }

    public static boolean httpDownload(String httpUrl, String saveFile) {
        int byteread = 0;

        URL url = null;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        }

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                // System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /** */
    /**
     * main方法测试
     *
     * @param s
     * @throws IOException
     */
    public static void main(String[] s) throws IOException {
        // ReadWriteFile.creatTxtFile();
        // ReadWriteFile.writeTxtFile("20080808:12:13");
        // ReadWriteFile.replaceTxtByStr("lxq_rx lxq_rx@126.com jinhuan10
        // 2010-12-01 1 0", "lxq_rx lxq_rx@126.com jinhuan10 2010-12-01 1 1",
        // "count.dat");
        // ReadWriteFile.readTxtFile("count.dat");
        ArrayList<String> fileNames = ReadWriteFile.getFiles("C:\\Users\\Administrator\\Downloads\\敏感词库2\\");
        StringBuffer sb = new StringBuffer();
        for (String fileName : fileNames) {
            if (fileName.endsWith(".txt")) {
                InputStream is = new FileInputStream(new File(fileName));
                InputStreamReader read = new InputStreamReader(is, "GBK");
                try {
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String txt = null;
                    while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
                        if (txt.indexOf("=") > 0) {
                            txt = txt.substring(0, txt.indexOf("="));
                        }

                        sb.append(txt.trim() + "\r\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    read.close(); // 关闭文件流
                }
            }
        }
        ReadWriteFile.writeTxtFile(sb.toString(), new File("C:\\Users\\Administrator\\Downloads\\1.txt"));
        System.out.println();
    }
}
