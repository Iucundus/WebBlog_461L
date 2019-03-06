// https://webblog461l.appspot.com
package guestbook;
import com.googlecode.objectify.ObjectifyService;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailServlet extends HttpServlet {

    static {
        ObjectifyService.register(Greeting.class);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@webblog461l.appspotmail.com", "461L Web Blog Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("john.koelling@utexas.edu", "Interested User"));
            msg.setSubject("Do you wish to subscribe?");
            msg.setText("Please respond with your preference");
            Transport.send(msg);
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
            // ...
        }

        /*
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
        */
    }

}