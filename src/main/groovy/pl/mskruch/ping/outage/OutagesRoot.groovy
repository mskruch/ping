package pl.mskruch.ping.outage

import com.googlecode.objectify.Key
import com.googlecode.objectify.Ref
import groovy.util.logging.Log
import pl.mskruch.ping.check.Check

import static com.googlecode.objectify.ObjectifyService.ofy

@Log
class OutagesRoot
{
	List<Check> find(checkId)
	{
		def checkKey = Key.create(Check.class, checkId)
		ofy().load().type(Outage.class).ancestor(checkKey).order('-started').list()
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
