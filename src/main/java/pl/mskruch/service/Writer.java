package pl.mskruch.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Writer
{
	private HttpServletResponse resp;

	public Writer(HttpServletResponse resp)
	{
		this.resp = resp;
		resp.setContentType("text/html");
	}

	public void header(String text) throws IOException
	{
		resp.getWriter().println("<h3>" + text + "</h3>");
	}

	public void paragraph(String text) throws IOException
	{
		resp.getWriter().println("<p>" + text + "</p>");
	}

	public void link(String link, String title) throws IOException
	{
		resp.getWriter()
			.println("<p><a href=\"" + link + "\">" + title + "</a></p>");
	}

	public void link(String link) throws IOException
	{
		link("/" + link, link);
	}
}
