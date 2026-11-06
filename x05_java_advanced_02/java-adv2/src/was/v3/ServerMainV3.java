package was.v3;

public class ServerMainV3 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        new HttpServerV3(PORT).start();
    }
}
