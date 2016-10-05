package seidl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Die Klasse UpdatePerson impliziert die Bean, welche die Funktion des Updaten einer Person übernimmt.
 * Sie besitzt Attribute, welche in eine vorhandene Entity gesetzt werden. Außerdem wird eine DatenbankSession erstellt
 * und die Daten übermittelt.
 * @author Maximilian Seidl
 * @version 2016-10-05
 **/
public class UpdatePerson {

    private Entity_Person oldPerson;
    private int number;
    private String message;
    private String newFirstname;
    private String newLastname;
    private int newAge;

    private SessionFactory factory;

    /**
     * Getter- und Setter- für die Attribute
     */


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getNewFirstname() {
        return newFirstname;
    }

    public void setNewFirstname(String newFirstname) {
        this.newFirstname = newFirstname;
    }

    public String getNewLastname() {
        return newLastname;
    }

    public void setNewLastname(String newLastname) {
        this.newLastname = newLastname;
    }

    public int getToAge() {
        return newAge;
    }

    public void setToAge(int toAge) {
        this.newAge = toAge;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Die Methode updatePerson baut eine Session zur Datenbank auf. Dabei benutzt sie konfigurierte Hibernate.cfg.xml,
     * in der die Verbindungsdaten sowie der Dialect eingetragen ist.
     *
     * Nach dem Aufbauen der Verbindung/Session wird ein Person-Objekt gesucht, welches zur eingegeben Nummer passt. Danach
     * beginnt die zweite Transaction, welche dann das Objekt oldPerson updatet und wieder in die Datenbank speichert.
     *
     * Für die Übertragung wird eine Transaction gestartet, welche aber nachher mittles commit() geschlossen werden muss.
     *
     * Beispiel von http://www.tutorialspoint.com/hibernate/hibernate_examples.htm
     */
    public void updatePerson() {
        Session session = null;

        try {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();

            Transaction transact1 = session.beginTransaction();
            oldPerson = session.find(Entity_Person.class, this.number);
            transact1.commit();

            Transaction transact2 = session.beginTransaction();
            Entity_Person person = session.find(Entity_Person.class, oldPerson.getNumber());

            person.setLastname(newLastname.equals("") ? oldPerson.getLastname() : newLastname);
            person.setFirstname(newFirstname.equals("") ? oldPerson.getFirstname() : newFirstname);
            person.setAge(newAge == 0 ? oldPerson.getAge() : newAge);

            session.saveOrUpdate(person);

            transact2.commit();

            session.close();

            this.message = "Person wurde erfolgreich upgedatet!";
        } catch (HibernateException e) {
            this.message = e.getMessage();
        } catch (NullPointerException e) {
            this.message = "Die gewünschte Nummer besitzt keine Person in der Datenbank!";
        }
    }
}