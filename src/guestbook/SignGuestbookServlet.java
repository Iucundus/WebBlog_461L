package guestbook;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import java.io.IOException;
import java.util.Date;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SignGuestbookServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();


        // We have one entity group per guestbook.Guestbook with all Greetings residing
        // in the same entity group as the guestbook.Guestbook to which they belong.
        // This lets us run a transactional ancestor query to retrieve all
        // Greetings for a given guestbook.Guestbook.  However, the write rate to each
        // guestbook.Guestbook should be limited to ~1/second.
        String guestbookName = req.getParameter("guestbookName");
        Key guestbookKey = KeyFactory.createKey("guestbook.Guestbook", guestbookName);
        String content = req.getParameter("address");
        Date date = new Date();
        Entity greeting = new Entity("Greeting", guestbookKey);
        greeting.setProperty("name", user);
        greeting.setProperty("date", date);
        greeting.setProperty("address", content);


        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(greeting);


        resp.sendRedirect("/guestbook.jsp?guestbookName=" + guestbookName);
    }
}