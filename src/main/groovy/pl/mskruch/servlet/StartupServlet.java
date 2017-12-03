package pl.mskruch.servlet;

import com.googlecode.objectify.ObjectifyService;
import pl.mskruch.ping.check.Check;
import pl.mskruch.ping.data.ConfigEntry;
import pl.mskruch.ping.user.User;
import pl.mskruch.ping.outage.Outage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StartupServlet extends HttpServlet {
    static {
        ObjectifyService.register(User.class);
        ObjectifyService.register(Check.class);
        ObjectifyService.register(ConfigEntry.class);
        ObjectifyService.register(Outage.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("foo");
    }
}
