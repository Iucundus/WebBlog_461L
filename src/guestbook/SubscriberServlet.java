// https://webblog461l.appspot.com
package guestbook;

import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.mail.Session;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SubscriberServlet extends HttpServlet {

    static {
        ObjectifyService.register(Subscriber.class);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String useraddress;
        useraddress = req.getParameter("address");
        if (useraddress == null) {
            resp.addHeader("body", "invalid address");
            return;
        }

        Subscriber subscriber1 = new Subscriber(UserServiceFactory.getUserService().getCurrentUser(), useraddress);
        ofy().save().entity(subscriber1).now();   // synchronous


        resp.sendRedirect("ofyguestbook.jsp");
    }

}