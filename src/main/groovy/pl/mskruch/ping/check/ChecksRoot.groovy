package pl.mskruch.ping.check

import com.googlecode.objectify.Key
import pl.mskruch.exception.NotFound

import java.util.logging.Logger

import static com.googlecode.objectify.ObjectifyService.ofy

class ChecksRoot
{
	static Logger logger = Logger.getLogger(ChecksRoot.class.getName());

	List<Check> all()
	{
		return ofy().load().type(Check.class).list();
	}

	boolean update(Long id, Status status)
	{
		def check = get(id)
		boolean changed = check.setStatus(status)
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

	Outage outage(Long checkId)
	{
		def checkKey = Key.create(Check.class, checkId)
		ofy().load().type(Outage.class).ancestor(checkKey).filter('finished = ', null).first().now()
	}
}
