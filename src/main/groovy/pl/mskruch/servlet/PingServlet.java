package pl.mskruch.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import pl.mskruch.ping.check.Check;
import pl.mskruch.ping.check.ChecksRoot;

public class PingServlet extends HttpServlet
{
	static Logger logger = Logger.getLogger(PingServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.fine("ping checks requested");

		ChecksRoot checks = new ChecksRoot();
		List<Check> all = checks.all();
		logger.fine(all.size() + " checks found");

		Queue queue = QueueFactory.getDefaultQueue();
		for (Check check : all) {
			if (Boolean.TRUE == check.getPaused()){
				continue;
			}
			queue.addAsync(TaskOptions.Builder.withUrl("/ping/" + check.getId())
				.method(TaskOptions.Method.GET));
		}
	}
}
