package pl.mskruch;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("<h3>ping</h3>");
        UserService userService = UserServiceFactory.getUserService();

        String thisUrl = req.getRequestURI();

        resp.setContentType("text/html");
        if (req.getUserPrincipal() != null) {
            resp.getWriter().println("<p>Hello, "
                    + req.getUserPrincipal().getName()
                    + "!  You can <a href=\""
                    + userService.createLogoutURL(thisUrl)
                    + "\">sign out</a>.</p>");
        } else {
            resp.getWriter().println("<p>Please <a href=\""
                    + userService.createLoginURL(thisUrl)
                    + "\">sign in</a>.</p>");
        }
    }
}
