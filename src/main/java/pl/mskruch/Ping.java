package pl.mskruch;

import com.googlecode.objectify.ObjectifyService;
import pl.mskruch.data.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class Ping extends HttpServlet {
    static Logger logger = Logger.getLogger(Ping.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        logger.warning("ping");

        resp.setContentType("text/html");
        if (req.getUserPrincipal() != null) {
            String name = req.getUserPrincipal().getName();

            logger.info("ping done by: " + name);
            // Or you can issue a query
            User fetched = ofy().load().type(User.class).filter("email", name).first().now();
            if (fetched != null){
                logger.info("fetched: " + fetched);
            } else {
                User user = new User(name);
                ofy().save().entity(user).now();    // async without the now()
                logger.info("created" + user);
            }


//            resp.getWriter().println("<p>Hello, "
//                    + req.getUserPrincipal().getName()
//                    + "!  You can <a href=\""
//                    + userService.createLogoutURL(thisUrl)
//                    + "\">sign out</a>.</p>");
        }

    }
}
