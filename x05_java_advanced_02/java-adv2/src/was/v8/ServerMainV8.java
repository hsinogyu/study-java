package was.v8;

import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServletV3;

import java.util.List;

public class ServerMainV8 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        List<Object> controllers = List.of(new SiteControllerV8(), new SearchControllerV8());
        AnnotationServletV3 annotationServlet = new AnnotationServletV3(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet);
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer httpServer = new HttpServer(PORT, servletManager);
        httpServer.start();
    }
}
