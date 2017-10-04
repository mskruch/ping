package pl.mskruch.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.mskruch.ping.service.Config;

public class ConfigServlet extends HttpServlet
{
	static Logger logger = Logger.getLogger(ConfigServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.fine("create config entry requested");

		BufferedReader reader = new BufferedReader(
			new InputStreamReader(req.getInputStream()));
		String key = reader.readLine();
		String value = reader.readLine();

		Long id = new Config().update(key, value);

		resp.getWriter().print(String.valueOf(id));
	}
}
