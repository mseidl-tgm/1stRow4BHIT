package seidl;

import javax.persistence.*;

//Anotation Entity bedeutet, das diese Klasse eine Entity in der DB ist.
@Entity
//Anotation Table  legt den Namen der Tabelle fest.
@Table (name = "person")

/**
 * Die Klasse Entity_Person stellt das Objekt dar, welches in die Datenbanke persistiert wird.
 * Sie besitzt die Getter- und Setter-Methoden für die Attribute und die toString-Methode wird überschrieben,
 * um die Daten richtig anzuzeigen.
 *
 * Beispiele: http://www.springbyexample.org/examples/one-to-many-jpa-hibernate-config-jpa-entity-config.html
 **/
public class Entity_Person
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //Attribute, welche in der DB gespeichert werden
    private int number;
    private String firstname;
    private String lastname;
    private int age;


    /**
     * Die Getter und Setter Methoden für die Attribute -autogeneriert
     */
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Es wird ein leerer Konstruktor benötigt um das Objekt anzulegen.
     */
    public Entity_Person( ) {
        super();
    }


    /**
     * Hier wird die toString-Methode von Object überschrieben um die Daten schön anzeigen zu können
     */
    @Override
    public String toString()
    {
        return "Person mit der Nummer=" + number + ", dem Vornamen=" + firstname + ", dem Nachnamen="
                + lastname + " und mit dem Alter=" + age + " ";
    }
}
