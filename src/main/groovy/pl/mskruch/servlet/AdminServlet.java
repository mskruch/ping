package pl.mskruch.servlet;

import pl.mskruch.ping.system.Config;
import pl.mskruch.ping.user.Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Users users = new Users(req);

        req.setAttribute("users", users.all());
        req.setAttribute("configEntries", new Config().all());
        req.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(req, resp);
    }
}
