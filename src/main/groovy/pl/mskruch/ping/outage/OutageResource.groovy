package pl.mskruch.ping.outage

import groovy.util.logging.Log
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.check.Outage

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@Log
@RequestMapping("/checks/{checkId}/outages")
class OutageResource
{
	Outages outages

	OutageResource(Outages outages)
	{
		this.outages = outages
	}

	@RequestMapping(method = GET)
	@ResponseBody
	List<Outage> list(@PathVariable("checkId") Long checkId)
	{
		outages.find(checkId)
	}
}
