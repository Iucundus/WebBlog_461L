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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class EmailServlet extends HttpServlet {

    static {
        ObjectifyService.register(Greeting.class);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        ObjectifyService.register(Greeting.class);
        List<Greeting> greetings = ObjectifyService.ofy().load().type(Greeting.class).list();
        Collections.sort(greetings);

        String msgtext = "";
        for (Greeting greeting : greetings) {
            msgtext += "Author: " + greeting.getUser() + "\n";
            msgtext += greeting.getContent();
            msgtext += "\n\n";
        }

        ObjectifyService.register(Subscriber.class);
        List<Subscriber> subs = ObjectifyService.ofy().load().type(Subscriber.class).list();
        Collections.sort(subs);

        List<String> emails = new ArrayList<String>();
        for (Subscriber s : subs) {
            emails.add(s.getAddress());
        }

        try {
            for (String address: emails) {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress("admin@webblog461l.appspotmail.com", "461L Web Blog Admin"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(address, "Interested User"));
                msg.setSubject("Do you wish to subscribe?");
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