package seidl;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Die Klasse ReadPerson impliziert die Bean, welche die Funktion des Lesen aller Personen übernimmt.
 * Sie besitzt ein Attribut, welches eine Liste voller Entity_Personen ist, um diese dementsprechend darzustellen.
 * @author Maximilian Seidl
 * @version 2016-10-05
 **/

public class ReadPerson {

    private List<Entity_Person> entity;

    private SessionFactory factory;


    /**
     * Die Getter- und Setter-Methode für das Attribut entity.
     */
    public List<Entity_Person> getEntries() {
        return entity;
    }

    public void setEntries(List<Entity_Person> entity) {
        this.entity = entity;
    }

    /**
     * Die Methode readEntities baut eine Session zur Datenbank auf. Dabei benutzt sie konfigurierte Hibernate.cfg.xml,
     * in der die Verbindungsdaten sowie der Dialect eingetragen ist.
     *
     * Nach dem Aufbauen der Verbindung/Session werden alle bestehenden Personen-Objekte aus der DB entnommen und eine Liste gespeichert.
     *
     * Für die Übertragung wird eine Transaction gestartet, welche aber nachher mittles commit() geschlossen werden muss.
     *
     * Die Entries, die die Liste bekommt, werden nachher in Form einer Tabelle ausgegeben.
     *
     * Beispiel von http://www.tutorialspoint.com/hibernate/hibernate_examples.htm
     */

    public void readEntities() {
        Session session = null;
        try {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();

            Transaction transact = session.beginTransaction();
            Query query = session.createQuery("from Entity_Person");
            entity = query.list();

            transact.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}