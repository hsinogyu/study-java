package was.httpserver;

public interface HttpServlet {

    void service(HttpRequest request, HttpResponse response);
}
