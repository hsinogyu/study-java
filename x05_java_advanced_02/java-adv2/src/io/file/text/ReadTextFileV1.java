package io.file.text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadTextFileV1 {

    private static final String PATH = "temp/hello2.txt";

    public static void main(String[] args) throws IOException {
        String writeString = "abc\n가나다";

        System.out.println("== Write String ===");
        System.out.println(writeString);

        // 파일에 저장
        Path file = Path.of(PATH);
        Files.writeString(file, writeString, StandardCharsets.UTF_8);

        // 파일에서 읽기
        String readString = Files.readString(file, StandardCharsets.UTF_8);

        System.out.println("== Read String ==");
        System.out.println(readString);
    }
}
