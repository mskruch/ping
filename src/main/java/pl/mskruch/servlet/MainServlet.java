package pl.mskruch.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.mskruch.service.Users;
import pl.mskruch.service.Writer;

public class MainServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		Users users = new Users(req);
		Writer writer = new Writer(resp);

		writer.header("ping");
		writer.link("manage");

		if (users.isLoggedIn()) {
		    writer.link(users.logoutURL(), "sign out");
		} else {
            writer.link(users.loginURL(), "sign in");
		}
	}
}
