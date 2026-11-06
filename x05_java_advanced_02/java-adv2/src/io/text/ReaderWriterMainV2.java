package io.text;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReaderWriterMainV2 {

    public static void main(String[] args) throws IOException {
        String writeString = "ABC";
        System.out.println("write String: " + writeString);

        // 파일에 쓰기
        FileOutputStream fos = new FileOutputStream(TextConst.FILE_NAME);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);

        osw.write(writeString);
        osw.close();

        // 파일에서 읽기
        FileInputStream fis = new FileInputStream(TextConst.FILE_NAME);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = isr.read()) != -1) {
            sb.append((char) ch);
        }
        isr.close();

        System.out.println("read String: " + sb);
    }
}
