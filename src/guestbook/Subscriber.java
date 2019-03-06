package guestbook;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import java.util.Date;


@Entity
public class Subscriber implements Comparable<Subscriber> {
    //@Parent Key<Guestbook> guestbookName;
    @Id Long id;
    @Index User name;
    @Index String address;
    @Index Date date;
    private Subscriber() {}
    public Subscriber(User name, String address) {
        this.name = name;
        this.address = address;
        date = new Date();
    }
    public User getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }

    @Override
    public int compareTo(Subscriber other) {
        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
    }
}