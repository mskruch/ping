package pl.mskruch.servlet;

import pl.mskruch.service.Config;
import pl.mskruch.service.Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Users users = new Users(req);
        if (req.getPathInfo() != null && req.getPathInfo().length() > 1){
            String id = req.getPathInfo();
            if (id.startsWith("/")){
                id = id.substring(1);
            }
            users.switchEnabled(id);
            resp.sendRedirect("/admin");
        }

        req.setAttribute("users", users.all());
        req.setAttribute("configEntries", new Config().all());
        req.getRequestDispatcher("/WEB-INF/jsp/admin.jsp").forward(req, resp);
    }
}
