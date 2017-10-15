package pl.mskruch.ping.check

import com.googlecode.objectify.Key
import com.googlecode.objectify.Work
import groovy.util.logging.Log
import pl.mskruch.exception.NotFound

import static com.googlecode.objectify.ObjectifyService.ofy
import static com.googlecode.objectify.ObjectifyService.run

@Log
class ChecksRoot
{
	List<Check> all()
	{
		return ofy().load().type(Check.class).list();
	}

	boolean update(Long id, Status status, Date checkTime)
	{
		ofy().transact({
			def check = get(id)
			log.fine("check found: $check")
			if (check.lastCheck > checkTime) {
				log.warning("last check was at $check.lastCheck and trying to update with check at $checkTime - ignore")
				return false
			}
			boolean changed = check.setStatus(status)
			ofy().save().entity(check)
			log.fine "saved changes in check $check"
			return changed
		})
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
		log.info("check created: " + check)
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
