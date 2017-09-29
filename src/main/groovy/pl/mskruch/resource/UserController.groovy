package pl.mskruch.resource

import com.google.appengine.api.log.InvalidRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.service.Checks;
import pl.mskruch.service.Users

import static java.util.logging.Logger.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@Controller
@RequestMapping("/admin/users")
class UserController {
    static def logger = getLogger(UserController.class.getName());

    Checks checks;
    Users users;

    UserController(Checks checks, Users users) {
        this.checks = checks
        this.users = users
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    delete(@PathVariable("id") Long id) {
        def user = users.get(id)
        def email = user.getEmail()

        if (checks.list(email).size() > 0){
            throw new InvalidRequestException("user has checks")
        }

        users.delete(id)

    }

}
