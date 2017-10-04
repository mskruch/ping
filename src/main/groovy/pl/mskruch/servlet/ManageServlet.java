package pl.mskruch.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.mskruch.ping.check.Checks;
import pl.mskruch.ping.service.Users;

public class ManageServlet extends HttpServlet
{
	static Logger logger = Logger.getLogger(ManageServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.fine("create check requested");
		Users users = new Users(req);
		if (!users.isEnabled()) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(req.getInputStream()));
		String line = reader.readLine();

		Long id = new Checks(req).add(line);

		resp.getWriter().print(String.valueOf(id));
	}
}
