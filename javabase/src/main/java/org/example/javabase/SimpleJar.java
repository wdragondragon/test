package org.example.javabase;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.24 16:31
 * @Description:
 */
public class SimpleJar {

    public static void main(String[] args) {
        get_class_path_file("C:\\Users\\10619\\Desktop\\跟222\\拖拉机\\class.txt","C:\\Users\\10619\\Desktop\\跟222\\拖拉机\\class_re.txt");
        deal_class("C:\\Users\\10619\\Desktop\\跟222\\拖拉机\\class_re.txt",
                "C:\\Users\\10619\\Desktop\\跟222\\拖拉机\\rt_source\\",
                "C:\\Users\\10619\\Desktop\\跟222\\拖拉机\\rt\\");
    }

    public static void get_class_path_file(String class_file_path,
                                           String re_class_file_path) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(class_file_path), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(re_class_file_path), StandardCharsets.UTF_8));
            String str = null;
            while ((str = in.readLine()) != null) {
                if ((str.contains("rt.jar")) && (str.contains("Loaded"))) {
                    String class_name = str.substring(8, str.indexOf("from") - 1);
                    String class_path = class_name.replace(".", "\\");
                    out.write(class_path);
                    out.newLine();
                }
            }
            out.flush();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static int deal_class(String class_path_file, String rt_dir, String new_rt_dir) {
        int sum = 0;
        File used_class = new File(class_path_file);
        if (used_class.canRead()) {
            String line = null;
            try{
                LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(used_class),
                        StandardCharsets.UTF_8));
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    int dirpos = line.lastIndexOf("\\");
                    if (dirpos > 0) {
                        String dir = new_rt_dir + line.substring(0, dirpos);
                        File fdir = new File(dir);
                        if (!fdir.exists())
                            fdir.mkdirs();
                        String sf = rt_dir + line + ".class";
                        String of = new_rt_dir + line + ".class";
                        boolean copy_ok = copy_file(sf.trim(), of.trim());
                        if (copy_ok)
                            sum++;
                        else {
                            System.out.println(line);
                        }

                    }

                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return sum;
    }
    public static boolean copy_file(String file1, String file2) {
        try
        {
            File file_in = new File(file1);
            File file_out = new File(file2);
            FileInputStream in1 = new FileInputStream(file_in);
            FileOutputStream out1 = new FileOutputStream(file_out);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in1.read(bytes)) != -1)
                out1.write(bytes, 0, c);
            in1.close();
            out1.close();
            return (true);
        } catch (IOException e) {
            e.printStackTrace();
            return (false);
        }
    }


//    public static void get_rt_jar(String rt_dir,String rt_jar_path){
//        try {
//            FileOutputStream fos3 = new FileOutputStream(new File(rt_jar_path));
//            File file = new File(rt_dir);
//            ZipOutputStream zos = new ZipOutputStream(fos3);
//            ZipUtils.dir_to_zip(file, zos, "");
//            zos.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private static void dir_to_zip(File sourceFile, ZipOutputStream zos, String name)
//            throws Exception {
//        boolean KeepDirStructure = true;
//        byte[] buf = new byte[2048];
//        if (sourceFile.isFile()) {
//            zos.putNextEntry(new ZipEntry(name));
//            int len;
//            FileInputStream in = new FileInputStream(sourceFile);
//            while ((len = in.read(buf)) != -1) {
//                zos.write(buf, 0, len);
//            }
//            // Complete the entry
//            zos.closeEntry();
//            in.close();
//        } else {
//            File[] listFiles = sourceFile.listFiles();
//
//            if (listFiles == null || listFiles.length == 0) {
//                if (KeepDirStructure) {
//                    zos.putNextEntry(new ZipEntry(name + "/"));
//                    zos.closeEntry();
//                }
//
//            } else {
//                for (File file : listFiles) {
//
//                    if (KeepDirStructure) {
//                        if (!name.equals("")){
//                            dir_to_zip(file, zos, name + "/" + file.getName());
//                        }else {
//                            dir_to_zip(file, zos, file.getName());
//                        }
//                    } else {
//                        dir_to_zip(file, zos, file.getName());
//                    }
//
//                }
//            }
//        }
//    }
}
