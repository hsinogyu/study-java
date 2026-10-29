package io.file;

import java.io.File;
import java.io.IOException;

public class OldFileMain {

    public static void main(String[] args) throws IOException {
        File file = new File("temp/example.txt");
        File directory = new File("temp/exampleDir");

        System.out.println("File exists: " + file.exists());

        // 파일 생성
        boolean created = file.createNewFile();
        System.out.println("File created: " + created);

        // 폴더 생성
        boolean dirCreated = directory.mkdir();
        System.out.println("Directory created: " + dirCreated);

        // 파일 삭제
//        boolean deleted = file.delete();
//        System.out.println("File deleted: " + deleted);

        System.out.println("Is File: " + file.isFile());
        System.out.println("Is Directory: " + directory.isDirectory());
        System.out.println("File Name: " + file.getName());
        System.out.println("File Size: " + file.length() + " bytes");

        // 파일 이름 변경
        File newFile = new File("temp/newExample.txt");
        boolean renamed = file.renameTo(newFile);
        System.out.println("File Renamed: " + renamed);

        // 파일 변경 일자 확인
        long lastModified = newFile.lastModified();
        System.out.println("File LastModified: " + lastModified);
    }
}
