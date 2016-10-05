package seidl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Die Klasse DeletePerson impliziert die Bean, welche die Funktion des Entfernen einer Person übernimmt.
 * Sie besitzt zwei Attribute, welche die Nummer des Eintrags festlegt,
 * welcher gelöscht werden soll und eine Nachricht für Informationen oder Fehler.
 **/
public class DeletePerson {


    private int number;
    private String message;

    private SessionFactory factory;

    /**
     * Getter- und Setter-Methoden der Attribute -autogeneriert
     */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Die Methode deletePersoninDB baut eine Session zur Datenbank auf. Dabei benutzt sie konfigurierte Hibernate.cfg.xml,
     * in der die Verbindungsdaten sowie der Dialect eingetragen ist.
     *
     * Nach dem Aufbauen der Verbindung/Session wird ein bestehendes Person-Objekt gelöscht.
     *
     * Für die Übertragung wird eine Transaction gestartet, welche aber nachher mittles commit() geschlossen werden muss.
     *
     * Bei Erfolg wird noch die Nachricht mit den Attributen gesetzt,
     * oder bei Fehlern trägt die Nachricht die entsprechende Fehlermeldung als Wert
     *
     * Beispiel von http://www.tutorialspoint.com/hibernate/hibernate_examples.htm
     */
    public void deletePersoninDB() {
        Session session = null;
        try {
            factory = new Configuration().configure().buildSessionFactory();
            session = factory.openSession();

            Transaction transact = session.beginTransaction();

            Entity_Person person = session.get(Entity_Person.class, this.number);
            session.delete(person);

            transact.commit();
            this.message = person + " wurde erfolgreich geloescht!";

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.message = "Es wurde keine Person zu der angegebenen Nummer gefunden.";
        } finally {
            session.close();
        }
    }
}