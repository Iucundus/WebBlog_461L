// https://webblog461l.appspot.com
package guestbook;
import com.google.appengine.api.datastore.*;
import com.googlecode.objectify.ObjectifyService;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class EmailServlet extends HttpServlet {

    static {
        ObjectifyService.register(Greeting.class);
    }

    /*
    Handles the GET request
    Sends an email to every subscriber on the list
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Subscriber> subs = ObjectifyService.ofy().load().type(Subscriber.class).list();
        Collections.sort(subs);

        List<String> emails = new ArrayList<String>();
        for (Subscriber s : subs) {
            emails.add(s.getAddress());
        }
        sendEmails(emails);
    }

    /*
    Sends a single email to a specified address
     */
    public void sendOneEmail(String address) {
        List<String> emails = new ArrayList<String>();
        emails.add(address);
        sendEmails(emails);
    }

    /*
    Common functionality used by both above methods
     */
    public void sendEmails(List<String> targets) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        ObjectifyService.register(Greeting.class);
        List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).list();
        Collections.sort(greetings);

        String msgtext = "Digest of the newest posts:\n\n";
        for (Greeting greeting : greetings) {
            if (greeting.getDate().before(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))))
                continue;
            msgtext += "Author: " + greeting.getUser() + "\n";
            msgtext += "Title: " + greeting.getTitle() + "\n";
            msgtext += greeting.getContent();
            msgtext += "\n\n";
        }

        try {
            for (String address: targets) {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("admin@webblog461l.appspotmail.com", "Dog Blog"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(address, "Interested User"));
                msg.setSubject("Dog Blog: Daily Digest");
                msg.setText(msgtext);
                Transport.send(msg);
            }
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
            // ...
        }
    }
}