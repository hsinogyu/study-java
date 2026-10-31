package network.tcp.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static util.MyLogger.log;

public class ClientV1 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        log("클라이언트 시작");

        Socket socket = new Socket("localhost", PORT);
        // - 클라이언트는 남는 포트 중에 자동으로 포트를 할당받음
        // - localport: 할당 받은 자신 포트
        log("소켓 연결: " + socket);

        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        // 서버에게 데이터 전송
        String toSend = "Hello";
        output.writeUTF(toSend); // 서버로 데이터 전달
        log("client -> server: " + toSend);

        // 서버로부터 데이터 수신
        String received = input.readUTF();
        log("client <- server: " + received);

        // 자원 정리
        log("연결 종료: " + socket);
        input.close();
        output.close();
        socket.close();
    }
}
