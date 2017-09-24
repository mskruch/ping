package pl.mskruch.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.mskruch.service.Checks;

public class ManageServlet extends HttpServlet
{
	static Logger logger = LoggerFactory.getLogger(ManageServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.debug("create check requested");
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(req.getInputStream()));
		String line = reader.readLine();

		Long id = new Checks(req).add(line);

		resp.getWriter().print(String.valueOf(id));
	}
}
