package seidl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Die Klasse CreatePerson impliziert die Bean, welche die Funktion des Erstellen einer neuen Person übernimmt.
 * Sie besitzt Attribute, welche von der Entity gesetzt werden. Außerdem wird eine DatenbankSession erstellt und die Daten übermittelt.
 * @author Maximilian Seidl
 * @version 2016-10-05
 **/

public class CreatePerson {

    //Attribute für die Werte, welche in die Datenbank gespeichert werden.
    private String firstname = "";
    private String lastname = "";
    private int age;
    //Dieser String zeigt alle Information an.
    private String message = "";
    private SessionFactory factory;

    /**
     * Getter von Vorname -autogeneriert
     * @return Den Wert des Vornamen
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Setter von Vorname - autogeneriert
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Getter von Nachname -autogeneriert
     * @return Den Wert des Nachnamen
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Setter von Vorname - autogeneriert
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Getter von Alter -autogeneriert
     * @return Den Wert des Alter
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter von Alter - autogeneriert
     * @param age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Getter von Nachricht -autogeneriert
     * @return Den Text der Nachricht
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter von Nachricht - autogeneriert
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Die Methode createPersoninDB baut eine Session zur Datenbank auf. Dabei benutzt sie konfigurierte Hibernate.cfg.xml,
     * in der die Verbindungsdaten sowie der Dialect eingetragen ist.
     *
     * Nach dem Aufbauen der Verbindung/Session wird ein neues Person-Objekt erstellt
     * und es werden die Attribute gesetzt.
     *
     * Für die Übertragung wird eine Transaction gestartet, welche aber nachher mittles commit() geschlossen werden muss.
     *
     * Bei Erfolg wird noch die Nachricht mit den Attributen gesetzt,
     * oder bei Fehlern trägt die Nachricht die entsprechende Fehlermeldung als Wert
     *
     * Beispiel von http://www.tutorialspoint.com/hibernate/hibernate_examples.htm
     */
    public void createPersoninDB() {
        Session session = null;
        try {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();
            Entity_Person entity = new Entity_Person( );
            entity.setFirstname( this.firstname );
            entity.setAge( this.age );
            entity.setLastname( this.lastname);

            Transaction transact = session.beginTransaction();

            //session.save speichert das Objekt in die DB.
            session.save(entity);

            transact.commit();

            this.message = entity.toString() + " erfolgreich eingetragen";
        }catch (HibernateException e) {
            this.message = e.getMessage();
        }finally {
            session.close();
        }
    }
}