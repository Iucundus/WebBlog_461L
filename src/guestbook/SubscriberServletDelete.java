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

public class SubscriberServletDelete extends HttpServlet {

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

        List<Subscriber> subscribers = ObjectifyService.ofy().load().type(Subscriber.class).list();
        Collections.sort(subscribers);

        for (Subscriber subscriber : subscribers) {
            if (subscriber.getAddress().equals(useraddress)) {
                ofy().delete().type(Subscriber.class).id(subscriber.getId());
            }
        }
    }

}