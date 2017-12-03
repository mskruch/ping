package pl.mskruch.ping.engine

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import com.google.appengine.api.taskqueue.TaskOptions
import groovy.time.TimeCategory
import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.check.Check
import pl.mskruch.ping.check.ChecksRoot
import pl.mskruch.ping.outage.OutagesRoot

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl
import static org.springframework.web.bind.annotation.RequestMethod.GET
import static pl.mskruch.common.DateUtils.secondsBetween
import static pl.mskruch.ping.check.Status.DOWN
import static pl.mskruch.ping.check.Status.UP

@Controller
@Log
@TupleConstructor
class PingController
{
	ChecksRoot checks
	OutagesRoot outages
	Pinger pinger

	@RequestMapping(value = "/ping/{id}", method = GET)
	@ResponseBody
	def processSingleCheck(@PathVariable("id") Long id)
	{
		def checkTime = new Date()
		log.info("processing $id at $checkTime")

		def check = checks.get(id)

		Result result = pinger.ping(check.getUrl());

		log.fine("ping " + check.getUrl() + " " + result.status);

		boolean changed = checks.update(check.id, result.status, checkTime)
		log.fine"status changed: $changed"
		if (changed) {
			log.info("status changed to $result.status")
//			notify(check, result, null)
		}

		def outage = outages.outage(id)
		if (result.status == DOWN && !outage){
			log.info "check failed, outage detected"
			outage = outages.createOutage(id, checkTime)
			if (!check.notificationDelay){
				notify(check, result, null)
				outage.notified = new Date()
				outages.save(outage)
			}
		} else if (result.status == DOWN && outage){
			log.info "another check failed, outage continue"
			if (!outage.notified && (!check.notificationDelay || (secondsBetween(checkTime, outage.started) > (check.notificationDelay as long)))) {
				def since = TimeCategory.minus(checkTime, outage.started).toString()
				log.info("check time $checkTime started $outage.started calculated string: $since")
				notify(check, result,since)
				outage.notified = new Date()
				outages.save(outage)
			}
		} else if (result.status == UP && !outage){
			log.fine "check passed"
		} else if (result.status == UP && outage){
			log.fine "outage is over"
			if (outage.notified) {
				notify(check, result, null)
			}
			outage.finished = checkTime
			outages.save(outage)
		}

		return result.status
	}

	private notify(Check check, Result result, String since) throws IOException
	{
		String name = check.name ?: check.url

		Queue queue = QueueFactory.getDefaultQueue()

		if (result.status == UP) {
			queue.addAsync(withUrl("/task/mail/up")
					.param("to", check.getOwnerEmail())
					.param("subject", "$name is $result.status")
					.param("url", check.url)
					.method(TaskOptions.Method.POST))
		} else {
			def reason = result.responseCode ?
					"response status code $result.responseCode" :
					"a lack of response due to $result.message"
			queue.addAsync(withUrl("/task/mail/down")
					.param("to", check.getOwnerEmail())
					.param("subject", "$name is $result.status")
					.param("url", check.url)
					.param("reason", reason)
					.param("since", since ?: '')
					.method(TaskOptions.Method.POST))
		}
	}
}
