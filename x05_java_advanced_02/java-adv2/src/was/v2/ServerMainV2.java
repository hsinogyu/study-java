package was.v2;

public class ServerMainV2 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        new HttpServerV2(PORT).start();
    }
}
