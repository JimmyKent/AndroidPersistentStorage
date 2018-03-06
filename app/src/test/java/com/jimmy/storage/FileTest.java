package com.jimmy.storage;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by jinguochong on 28/02/2018.
 */
/*
public class FileTest {

    @Test
    public void test() {
        writeTxtFile();
        readTxtFile();

    }

    //http://www.baeldung.com/java-write-to-file
    private static void writeTxtFile(String filePath, int[][] arrs) {
        try {

//1
            String str = "Hello";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);

            writer.close();

//2
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("Some String");
            printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);
            printWriter.close();


            //3



            String str = "Hello";
            FileOutputStream outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);

            outputStream.close();



            String encoding = "utf-8";
            File file = new File(filePath, "w");

            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            int i = 0;
            int j = 0;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                //System.out.println(lineTxt);
                String[] lineArr = lineTxt.split(",");
                for (String num : lineArr) {
                    arrs[i][j] = Integer.valueOf(num);
                    j++;
                }
                i++;
                j = 0;
            }
            read.close();
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

    private static void readTxtFile(String filePath, int[][] arrs) {

        try {
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                int i = 0;
                int j = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    //System.out.println(lineTxt);
                    String[] lineArr = lineTxt.split(",");
                    for (String num : lineArr) {
                        arrs[i][j] = Integer.valueOf(num);
                        j++;
                    }
                    i++;
                    j = 0;
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }
}*/
