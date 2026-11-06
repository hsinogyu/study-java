package io.streams;

import java.io.*;

public class DataStreamEtcMain {

    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("temp/data.dat");
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeUTF("회원 A"); // UTF-8로 저장함 (3바이트)
        dos.writeInt(20); // 4바이트로 저장함
        dos.writeDouble(10.5); // 8바이트로 저장함
        dos.close();

        FileInputStream fis = new FileInputStream("temp/data.dat");
        DataInputStream dis = new DataInputStream(fis);

        // 반드시 저장한 순서대로 읽어야 함
        String n1 = dis.readUTF();
        int n2 = dis.readInt();
        double n3 = dis.readDouble();
        dis.close();

        System.out.println("n1 = " + n1);
        System.out.println("n2 = " + n2);
        System.out.println("n3 = " + n3);
    }
}
