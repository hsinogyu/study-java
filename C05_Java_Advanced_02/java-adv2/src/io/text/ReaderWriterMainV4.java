package io.text;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReaderWriterMainV4 {

    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) throws IOException {
        String writeString = "abc\n가나다";
        System.out.println("== Write String ==");
        System.out.println(writeString);

        // 파일에 쓰기
        BufferedWriter bw = new BufferedWriter(new FileWriter(TextConst.FILE_NAME, StandardCharsets.UTF_8), BUFFER_SIZE);

        bw.write(writeString);
        bw.close();

        // 파일에서 읽기
        BufferedReader br = new BufferedReader(new FileReader(TextConst.FILE_NAME, StandardCharsets.UTF_8), BUFFER_SIZE);

        StringBuilder sb = new StringBuilder();
        String line;
        int cnt = 0;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
            cnt++;
        }
        br.close();

        System.out.println("== Read String ==");
        System.out.println(sb);
        System.out.println("cnt: " + cnt); // 2번 읽음(2줄)
    }
}
