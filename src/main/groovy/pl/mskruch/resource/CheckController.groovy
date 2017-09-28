package pl.mskruch.resource

import groovy.transform.ToString
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pl.mskruch.data.Check
import pl.mskruch.service.Checks

import java.util.logging.Logger

import static org.springframework.web.bind.annotation.RequestMethod.DELETE
import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.PATCH

@Controller
@RequestMapping("/checks")
class CheckController {
    static Logger logger = Logger.getLogger(CheckController.class.getName());

    Checks checks;

    CheckController(Checks checks) {
        this.checks = checks
    }

    @RequestMapping(value = "/{id}", method = PATCH)
    @ResponseBody
    Check update(@PathVariable("id") Long id, @RequestBody Check body) {
        logger.info("update check " + id + " with " + body)
        body.id = id
        checks.patch(body)
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    Check get(@PathVariable("id") Long id){
        checks.get(id)
    }

    @RequestMapping(value = "/", method = GET)
    @ResponseBody
    List<Check> list(){
        checks.list()
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    delete(@PathVariable("id") Long id){
        checks.delete(id)
    }
}

@ToString
class CheckUpdate {
    Long delay
}