package pl.mskruch.service

import pl.mskruch.exception.NotFound;

import static com.googlecode.objectify.ObjectifyService.ofy

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired;

import pl.mskruch.ping.check.Check;

class Checks
{
	static Logger logger = Logger.getLogger(Checks.class.getName());

	private final Auth auth

	@Autowired
	Checks(Auth auth)
	{
		this.auth = auth
	}

	/* only for servlets */
	@Deprecated
	Checks(HttpServletRequest req){
		this.auth = new Auth(req)
	}

	List<Check> list()
	{
		return ofy().load().type(Check.class)
				.filter("ownerEmail", auth.email()).list();
	}


	public List<Check> all()
	{
		return ofy().load().type(Check.class).list();
	}

	public Long add(String url)
	{
		logger.fine("create check for " + url);
		Check check = new Check(auth.email(), url);
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

	public Check get(Long id)
	{
		Check check = ofy().load().type(Check.class).id(id).now();
		if (check == null) {
			throw new NotFound("check",id);
		}
		if (!auth.email().equals(check.getOwnerEmail())) {
			throw new NotFound("check",id);
		}
		return check;
	}

	Check patch(Check patch)
	{
		Check check = get(patch.getId());
		if (patch.getNotificationDelayInMilliseconds() != null) {
			check.setNotificationDelayInMilliseconds(
					patch.getNotificationDelayInMilliseconds());
		}
		if (patch.name) {
			check.setName(patch.name)
		}
		ofy().save().entity(check).now();
		return check;
	}

	def delete(Long id)
	{
		try {
			Check check = get(id);
			ofy().delete().entity(check);
		} catch (NotFound e) {
			/* already deleted - still fine */
		}
	}
}
