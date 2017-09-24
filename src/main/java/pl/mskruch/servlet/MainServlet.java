package pl.mskruch.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.mskruch.service.Checks;
import pl.mskruch.service.Users;
import pl.mskruch.service.Writer;

public class MainServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		Users users = new Users(req);
		Checks checks = new Checks(req);

		req.setAttribute("checks", checks.list());
		req.setAttribute("logoutUrl", users.logoutURL());

		req.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(req, resp);
	}
}
