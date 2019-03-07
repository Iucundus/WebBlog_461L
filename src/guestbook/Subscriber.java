package guestbook;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;


@Entity
public class Subscriber implements Comparable<Subscriber> {
    //@Parent Key<Guestbook> guestbookName;
    @Id Long id;
    @Index User user;
    @Index String address;
    @Index Date date;
    private Subscriber() {}
    public Subscriber(User name, String address) {
        this.user = name;
        this.address = address;
        date = new Date();
    }
    public User getUser() {
        return user;
    }
    public String getAddress() {
        return address;
    }
    public Long getId() { return id; }

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