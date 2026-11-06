package io.file.text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ReadTextFileV2 {

    private static final String PATH = "temp/hello2.txt";

    public static void main(String[] args) throws IOException {
        String writeString = "abc\n가나다";

        System.out.println("== Write String ===");
        System.out.println(writeString);

        // 파일에 저장
        Path file = Path.of(PATH);
        Files.writeString(file, writeString, StandardCharsets.UTF_8);

        // 파일에서 읽기
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        System.out.println("== Read String ==");
//        for (int i = 0; i < lines.size(); i++) {
//            System.out.println((i + 1) + ": " + lines.get(i));
//        }

        Stream<String> lineStream = Files.lines(file, StandardCharsets.UTF_8);
        lineStream.forEach(line -> System.out.println(line)); // 한줄씩 메모리에 올림
        lineStream.close();
    }
}
