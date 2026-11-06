package io.start;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteArrayStreamMain {

    public static void main(String[] args) throws IOException {
        byte[] input = {1, 2, 3};

        // 메모리에 쓰기
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(input);
        baos.close();

        // 메모리에서 읽기
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] readAllBytes = bais.readAllBytes();
        System.out.println(Arrays.toString(readAllBytes));
        bais.close();
    }
}
