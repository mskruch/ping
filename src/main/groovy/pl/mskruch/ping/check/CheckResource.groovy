package pl.mskruch.ping.check

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import java.util.logging.Logger

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/checks")
class CheckResource
{
	static Logger logger = Logger.getLogger(CheckResource.class.getName());

	Checks checks;

	CheckResource(Checks checks)
	{
		this.checks = checks
	}

	@RequestMapping(value = "/{id}", method = PATCH)
	@ResponseBody
	Check update(@PathVariable("id") Long id, @RequestBody Check body)
	{
		logger.info("update check " + id + " with " + body)
		body.id = id
		checks.patch(body)
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