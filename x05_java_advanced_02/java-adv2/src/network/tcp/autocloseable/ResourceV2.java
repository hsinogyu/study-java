package network.tcp.autocloseable;

public class ResourceV2 implements AutoCloseable { // try-with-resources를 사용하려면 이게 있어야함

    private String name;

    public ResourceV2(String name) {
        this.name = name;
    }

    public void call() {
        System.out.println(name + " call");
    }

    public void callEx() throws CallException {
        System.out.println(name + " callEx");
        throw new CallException(name + " ex");
    }

    @Override // AutoCloseable
    public void close() throws CloseException {
        System.out.println(name + " close");
        throw new CloseException(name + " ex");
    }
}
