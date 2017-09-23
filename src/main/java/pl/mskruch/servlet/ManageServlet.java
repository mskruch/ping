package pl.mskruch.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mskruch.data.Check;
import pl.mskruch.service.Checks;
import pl.mskruch.service.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ManageServlet extends HttpServlet
{
	static Logger logger = LoggerFactory.getLogger(ManageServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		Writer writer = new Writer(resp);
		writer.header("manage");

		Checks checks = new Checks(req);
		List<Check> list = checks.list();
		for (Check check : list) {
			writer.paragraph(check.toString());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		logger.debug("create check requested");
		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
		String line = reader.readLine();

		new Checks(req).add(line);

		new Writer(resp).paragraph("check " + line + " created");
	}
}
