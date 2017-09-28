package pl.mskruch.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import pl.mskruch.data.Check;

public class Checks
{
	static Logger logger = Logger.getLogger(Checks.class.getName());
	private HttpServletRequest req;

	@Autowired
	public Checks(HttpServletRequest req)
	{
		this.req = req;
	}

	public List<Check> list()
	{
		return ofy().load().type(Check.class)
			.filter("ownerEmail", currentUser()).list();
	}

	public List<Check> all()
	{
		return ofy().load().type(Check.class).list();
	}

	private String currentUser()
	{
		return req.getUserPrincipal().getName();
	}

	public Long add(String url)
	{
		logger.fine("create check for " + url);
		Check check = new Check(currentUser(), url);
		ofy().save().entity(check).now(); // async without the now()
		logger.info("check created: " + check);
		return check.getId();
	}

	public boolean update(Check check, Result result)
	{
		boolean changed = check.setStatus(result.status());
		ofy().save().entity(check).now();
		return changed;
	}

	public Check updateDelay(Long id, Long delay)
	{
		Check check = ofy().load().type(Check.class).id(id).now();
		check.setNotificationDelayInMilliseconds(delay);
		ofy().save().entity(check).now();
		return check;
	}
}
