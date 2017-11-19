package pl.mskruch.ping.outage

import com.googlecode.objectify.Key
import groovy.util.logging.Log
import pl.mskruch.ping.check.Check
import pl.mskruch.ping.check.Outage

import static com.googlecode.objectify.ObjectifyService.ofy

@Log
class OutagesRoot
{
	def find(checkId)
	{
		def checkKey = Key.create(Check.class, checkId)
		ofy().load().type(Outage.class).ancestor(checkKey).list()
	}
}
