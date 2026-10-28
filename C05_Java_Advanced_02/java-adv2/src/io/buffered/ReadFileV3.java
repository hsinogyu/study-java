package io.buffered;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadFileV3 {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(BufferedConst.FILE_NAME);
        BufferedInputStream bis = new BufferedInputStream(fis, BufferedConst.BUFFER_SIZE);

        long startTime = System.currentTimeMillis();

        int fileSize = 0;
        int data = 0;
        while ((data = bis.read()) != -1) { // 약 1000만 번 호출
            fileSize++;
        }
        bis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File name: " + BufferedConst.FILE_NAME);
        System.out.println("File size: " + fileSize / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
