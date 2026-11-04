package was.httpserver;

import was.httpserver.servlet.DiscardServlet;
import was.v5.HomeServlet;
import was.v5.SearchServlet;
import was.v5.Site1Servlet;
import was.v5.Site2Servlet;

public class ServerMain {

    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {

        ServletManager servletManager = new ServletManager();
        servletManager.add("/", new HomeServlet());
        servletManager.add("/site1", new Site1Servlet());
        servletManager.add("/site2", new Site2Servlet());
        servletManager.add("/search", new SearchServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer httpServer = new HttpServer(PORT, servletManager);
        httpServer.start();
    }
}
