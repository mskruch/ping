package pl.mskruch.ping.check

import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pl.mskruch.exception.BadRequest
import pl.mskruch.ping.user.Users

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@Log
@RequestMapping(["/checks", "/api/checks"])
@TupleConstructor
class CheckResource
{
	Checks checks

	@RequestMapping(method = POST)
	@ResponseBody
	def create(@RequestBody request)
	{
		log.info "create check with $request"
		if (!request.url?.trim()) {
			throw new BadRequest('url is required')
		}
		if (checks.list().size() >= 10){
			throw new BadRequest('limit of 10 checks reached')
		}
		checks.create(request.url, request.name)
	}

	@RequestMapping(value = "/{id}", method = PATCH)
	@ResponseBody
	Check patch(@PathVariable("id") Long id, @RequestBody request)
	{
		log.info("update check $id with $request (${request.getClass()})")
		def name = request.name
		def paused = request.paused
		def notificationDelay = request.containsKey('notificationDelay') ? (request.notificationDelay ? request.notificationDelay as Long : 0) : null
		checks.patch(id, name, notificationDelay, paused)
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	Check get(@PathVariable("id") Long id)
	{
		checks.get(id)
	}

	@RequestMapping(method = GET)
	@ResponseBody
	List<Check> list()
	{
		checks.list()
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void delete(@PathVariable("id") Long id)
	{
		checks.delete(id)
	}
}