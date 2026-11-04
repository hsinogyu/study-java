package was.v4;

public class ServerMainV4 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        new HttpServerV4(PORT).start();
    }
}
