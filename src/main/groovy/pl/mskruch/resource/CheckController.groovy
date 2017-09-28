package pl.mskruch.resource

import groovy.transform.ToString
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pl.mskruch.data.Check
import pl.mskruch.service.Checks

import java.util.logging.Logger

@Controller
@RequestMapping("/checks")
class CheckController {
    static Logger logger = Logger.getLogger(CheckController.class.getName());

    Checks checks;

    CheckController(Checks checks) {
        this.checks = checks
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    Check update(@PathVariable("id") Long checkId, @RequestBody CheckUpdate body) {
        logger.info("update check " + checkId + " with " + body)
        if (body.delay != null) {
            checks.updateDelay(checkId, body.delay)
        } else {
            null
        }
    }
}

@ToString
class CheckUpdate {
    Long delay
}