// https://ee461lt6.appspot.com/ofyguestbook.jsp
package guestbook;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class OfySignGuestbookServlet extends HttpServlet {

    static {
        ObjectifyService.register(Greeting.class);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user == null)
            return;

        String guestbookName = req.getParameter("guestbookName");
        Key guestbookKey = KeyFactory.createKey("guestbook.Guestbook", guestbookName);
        String content = req.getParameter("content");
        Date date = new Date();

        Greeting greeting = new Greeting(user, content, guestbookName);
        ofy().save().entity(greeting).now();   // synchronous
        resp.sendRedirect("/ofyguestbook.jsp?guestbookName=" + guestbookName);
    }

}