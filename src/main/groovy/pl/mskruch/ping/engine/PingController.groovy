package pl.mskruch.ping.engine

import com.google.appengine.api.taskqueue.Queue
import com.google.appengine.api.taskqueue.QueueFactory
import com.google.appengine.api.taskqueue.TaskOptions
import groovy.util.logging.Log
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.check.Check
import pl.mskruch.ping.check.ChecksRoot

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl
import static org.springframework.web.bind.annotation.RequestMethod.GET


@Controller
@Log
class PingController
{
	ChecksRoot checks

	PingController(ChecksRoot checks)
	{
		this.checks = checks
	}

	@RequestMapping(value = "/ping/{id}", method = GET)
	@ResponseBody
	def processSingleCheck(@PathVariable("id") Long id)
	{
		def checkTime = new Date()
		log.info("processing $id at $checkTime")

		def check = checks.get(id)

		Pinger pinger = new Pinger();
		Result result = pinger.ping(check.getUrl());

		log.fine("ping " + check.getUrl() + " " + result.status());

		boolean updated = checks.update(check.id, result.status(), checkTime)
		if (updated) {
			notify(check, result, checkTime)
		}
	}

	private notify(Check check, Result result, Date checkTime) throws IOException
	{
		String name = check.name ?: check.url
		name + " is " + result.status()

		def durationString = result.elapsedInMilliseconds() != null ? (result.elapsedInMilliseconds() / 1000) + " seconds" : ""
		def body = "At $checkTime, $check.url was $result.status\n" +
				"Status code: $result.responseCode\n" +
				"Time: $durationString\n" +
				"Details: $result.message" //TODO: html

		Queue queue = QueueFactory.getDefaultQueue()
		queue.addAsync(withUrl("/task/mail")
				.param("to", check.getOwnerEmail())
				.param("subject", "$name is $result.status")
				.param("body", body)
				.method(TaskOptions.Method.POST))
	}
}
