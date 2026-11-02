package network.tcp.autocloseable;

public class ResourceCloseMainV2 {

    public static void main(String[] args) {
        try {
            logic();
        } catch (CallException e) {
            System.out.println("CallException 예외 처리");
            throw new RuntimeException(e);
        } catch (CloseException e) {
            System.out.println("CloseException 에외 처리");
            throw new RuntimeException(e);
        }
    }

    private static void logic() throws CallException, CloseException {

        ResourceV1 resource1 = null;
        ResourceV1 resource2 = null;

        try {
            resource1 = new ResourceV1("resource1"); // 만약 여기서 예외가 터진다면
            resource2 = new ResourceV1("resource2"); // 2번은 null인 상태로 finally로 가서 문제가 발생할 수 있음

            resource1.call();
            resource1.callEx(); // CallException
        } catch (CallException e) {
            System.out.println("ex: " + e);
            throw e;
        } finally {
            System.out.println("자원 정리"); // 호출 됨

            if (resource2 != null) {
                resource2.closeEx(); // 자원 정리 중 예외 발생
                // - CallException이 던져지는게 아니라 CloseException이 던져짐
            }

            if (resource1 != null) {
                resource1.closeEx(); // 호출 안됨
            }
        }
    }
}
