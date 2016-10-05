package seidl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UpdatePerson {

    private Entity_Person loadedPerson;
    private int toId;
    private String status;
    private String toFirstName;
    private String toLastName;
    private int toAge;

    /**
     * Gets the status of the transaction
     * @return The transaction's status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the transaction
     * @param status - A string used to set the transaction's status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the id of the Person which will be changed
     * @return  The id of the Person which will be changed
     */
    public int getToId() {
        return toId;
    }

    /**
     * Sets the id of the Person which will be changed to identify him
     * @param toId - A int used to set the person's id
     */
    public void setToId(int toId) {
        this.toId = toId;
    }

    /**
     * Gets the firstname (new) to which the firstname (old) will be changed to
     * @return The firstname (new)
     */
    public String getToFirstName() {
        return toFirstName;
    }

    /**
     * Sets the new firstname of the selected Person
     * @param toFirstName - A string used to set the person's firstname
     */
    public void setToFirstName(String toFirstName) {
        this.toFirstName = toFirstName;
    }

    /**
     * Gets the lastname (new) to which the lastname (old) will be changed to
     * @return The lastname (new)
     */
    public String getToLastName() {
        return toLastName;
    }

    /**
     * Sets the new lastname of the selected Person
     * @param toLastName - A string used to set the person's lastname
     */
    public void setToLastName(String toLastName) {
        this.toLastName = toLastName;
    }

    /**
     * Gets the age (new) to which the age (old) will be changed to
     * @return The age (new)
     */
    public int getToAge() {
        return toAge;
    }

    /**
     * Sets the new age of the selected Person
     * @param toAge - A int used to set the person's age
     */
    public void setToAge(int toAge) {
        this.toAge = toAge;
    }

    /**
     * Opens a Session to the Database, loads the data of the Person which will be changed, starts a new transaction to prevent inconsistent Data.
     * If the Value of tofirstname and tolastname is empty the value of the field in the database will not be changed
     * If the toage is set to 0 the value will not be changed
     *
     * After the settings were made the data is saved and the status is set to inform the User.
     */
    public void updatePerson() {

        try (
                SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
                Session session = sessionFactory.openSession();
        ) {


            this.loadPerson(session);

            //create transaction
            Transaction transact = session.beginTransaction();

            Entity_Person person = session.find(Entity_Person.class, loadedPerson.getNumber());

            person.setLastname(toLastName.equals("") ? loadedPerson.getLastname() : toLastName);
            person.setFirstname(toFirstName.equals("") ? loadedPerson.getFirstname() : toFirstName);
            person.setAge(toAge == 0 ? loadedPerson.getAge() : toAge);

            session.saveOrUpdate(person);

            transact.commit();

            session.close();

            this.status = "Person updated successfully!";
        } catch (HibernateException e) {
            this.status = e.getMessage();
        } catch (NullPointerException e) {
            this.status = "Selected Number didn't match a Person in the Database";
        }
    }

    private void loadPerson(Session s) {
        try {
            //create transaction
            Transaction transact = s.beginTransaction();
            loadedPerson = s.find(Entity_Person.class, this.toId);
            transact.commit();
        } catch (Exception e) {
            this.status = "Something went wrong while loading the Person!";
        }

    }
}