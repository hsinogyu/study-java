package io.text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReaderWriterMainV1 {

    public static void main(String[] args) throws IOException {
        String writeString = "ABC";

        // 문자 - utf8 인코딩
        byte[] writeBytes = writeString.getBytes(StandardCharsets.UTF_8);
        System.out.println("write String: " + writeString);
        System.out.println("write bytes: " + Arrays.toString(writeBytes));

        // 파일에 쓰기
        FileOutputStream fos = new FileOutputStream(TextConst.FILE_NAME);
        fos.write(writeBytes);
        fos.close();

        // 파일에서 읽기
        FileInputStream fis = new FileInputStream(TextConst.FILE_NAME);
        byte[] readBytes = fis.readAllBytes(); // 여기서는 UTF_8의 형태로 들고 있음
        fis.close();

        // byte -> String UTF-8 디코딩
        String readString = new String(readBytes, StandardCharsets.UTF_8);
        System.out.println("read String:  " + readString);
        System.out.println("read bytes: " + Arrays.toString(readBytes));
    }
}
