package pl.mskruch.ping.check

import pl.mskruch.exception.NotFound
import pl.mskruch.ping.service.Result

import java.util.logging.Logger

import static com.googlecode.objectify.ObjectifyService.ofy

class ChecksRoot
{
	static Logger logger = Logger.getLogger(ChecksRoot.class.getName());

	List<Check> all()
	{
		return ofy().load().type(Check.class).list();
	}

	boolean update(Long id, Result result)
	{
		def check = get(id)
		boolean changed = check.setStatus(result.status())
		ofy().save().entity(check).now()
		return changed
	}

	List<Check> ownedBy(String email)
	{
		return ofy().load().type(Check.class)
				.filter("ownerEmail", email).list();
	}

	Check get(long id)
	{
		Check check = ofy().load().type(Check.class).id(id).now();
		if (check == null) {
			throw new NotFound("check", id);
		}
		check
	}

	Long create(String email, String url)
	{
		Check check = new Check(email, url)
		ofy().save().entity(check).now()
		logger.info("check created: " + check)
		check.getId()
	}

	def delete(Check check)
	{
		try {
			ofy().delete().entity(check);
		} catch (NotFound e) {
			/* already deleted - fine */
		}
	}

	Check save(Check check)
	{
		ofy().save().entity(check).now()
		check
	}
}
