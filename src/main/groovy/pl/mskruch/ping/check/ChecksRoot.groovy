package pl.mskruch.ping.check

import com.google.appengine.api.memcache.MemcacheServiceFactory
import com.googlecode.objectify.Key
import com.googlecode.objectify.Ref
import groovy.util.logging.Log
import pl.mskruch.exception.NotFound

import static com.googlecode.objectify.ObjectifyService.ofy

@Log
class ChecksRoot
{
	private checksCache = MemcacheServiceFactory.getMemcacheService("checks")

	List<Check> all()
	{
		return ofy().load().type(Check.class).list();
	}

	boolean update(Long id, Status status, Date checkTime)
	{
		def changed = false
		ofy().transact {
			def check = get(id)
			log.fine("check found: $check")
			if (check.lastCheck > checkTime) {
				log.warning("last check was at $check.lastCheck and trying to update with check at $checkTime - ignore")
				return
			}
			changed = check.status = status
			ofy().save().entity(check)
			checksCache.delete(check.ownerEmail)
			log.fine "saved changes in check $check returning $changed"
		}
		log.fine("returning $changed from the update method")
		return changed
	}

	List<Check> ownedBy(String email)
	{
		def cached = checksCache.get(email)
		return cached ?: fetchOwnedBy(email);
	}

	private List<Check> fetchOwnedBy(String email)
	{
		log.info "fetching checks for $email"
		def list = ofy().load().type(Check.class).filter("ownerEmail", email).list()
		checksCache.put(email, list)
		return list
	}

	Check get(long id)
	{
		Check check = ofy().load().type(Check.class).id(id).now();
		if (check == null) {
			throw new NotFound("check", id);
		}
		check
	}

	def create(email, url, name)
	{
		checksCache.delete(email)
		Check check = new Check(email, url, name)
		ofy().save().entity(check).now()
		log.info("check created: " + check)
		return check
	}

	def delete(Check check)
	{
		checksCache.delete(check.ownerEmail)
		ofy().delete().entity(check)
		ofy().delete().entities(outages(check.id))
	}

	Check save(Check check)
	{
		checksCache.delete(check.ownerEmail)
		ofy().save().entity(check).now()
		check
	}

	Outage save(Outage outage)
	{
		ofy().save().entity(outage).now()
		outage
	}

	Outage createOutage(Long checkId, Date checkTime)
	{
		Key<Check> checkKey = Key.create(Check.class, checkId)
		Ref<Check> ref = Ref.create(checkKey)
		def outage = new Outage(ref, checkTime)

		ofy().save().entity(outage).now()
		log.fine "outage created $outage"
		return outage
	}

	Outage outage(Long checkId)
	{
		def checkKey = Key.create(Check.class, checkId)
		ofy().load().type(Outage.class).ancestor(checkKey).filter('finished = ', null).first().now()
	}

	List<Outage> outages(Long checkId)
	{
		def checkKey = Key.create(Check.class, checkId)
		ofy().load().type(Outage.class).ancestor(checkKey).list()
	}
}
