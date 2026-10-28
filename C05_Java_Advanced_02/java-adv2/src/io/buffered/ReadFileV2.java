package io.buffered;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadFileV2 {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(BufferedConst.FILE_NAME);

        long startTime = System.currentTimeMillis();

        byte[] buffer = new byte[BufferedConst.BUFFER_SIZE];
        int fileSize = 0;
        int readCount = 0;

        while ((readCount = fis.read(buffer, 0, BufferedConst.BUFFER_SIZE)) != -1) {
            fileSize += readCount;
        }

        fis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File name: " + BufferedConst.FILE_NAME);
        System.out.println("File size: " + fileSize / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
