package io.buffered;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadFileV4 {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(BufferedConst.FILE_NAME);

        long startTime = System.currentTimeMillis();

        byte[] buffer = fis.readAllBytes();

        int fileSize = buffer.length;

        fis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File name: " + BufferedConst.FILE_NAME);
        System.out.println("File size: " + fileSize / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
