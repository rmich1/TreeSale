package model;

import exception.InvalidPrimaryKeyException;
import model.EntityBase;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Scout extends EntityBase {
    //Table Name Scout
    private static final String myTableName = "Scout";

    // GUI Components

    private String updateStatusMessage = "";

    protected Properties dependencies;
    //Empty Constructor
    public Scout(){
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }
    //Looks for a scoutID this will never be used
    public Scout(String scoutId)
            throws InvalidPrimaryKeyException {
        //calling the constructor to the EntityBase by passing in the name of the table
        super(myTableName);
        setDependencies();
        //SQL Query to get all scouts with scoutID's
        String query = "SELECT * FROM " + myTableName + " WHERE (scoutId = " + scoutId + ")";

        //creates a vector of all objects where the information from the table is related to the primary key
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one scout
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple scouts matching id : "
                        + scoutId + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedAccountData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();
                //This will get the name of our column tables
                Enumeration allKeys = retrievedAccountData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    //nextKey is the column name, nextValue is the data associated with key
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedAccountData.getProperty(nextKey);


                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);

                    }
                }

            }
        }
        // If no account found for this user name, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No scouts matching id : "
                    + scoutId + " found.");
        }

    }
    //This is to add something into the database that doesn't exist
    public Scout(Properties props) {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        if (props != null) {
            Enumeration allKeys = props.propertyNames();
            while (allKeys.hasMoreElements() == true) {
                String nextKey = (String) allKeys.nextElement();
                String nextValue = props.getProperty(nextKey);

                if (nextValue != null) {
                    persistentState.setProperty(nextKey, nextValue);

                }
            }
        }
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies() {

        dependencies = new Properties();
        //dependencies.setProperty("InsertNewScout", "InsertError");
        //dependencies.setProperty("SearchScouts", "SearchError");

        myRegistry.setDependencies(dependencies);
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {

       /* if (key.equals("scoutId") == true)
            return persistentState.getProperty("scoutId");
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;
        if(key.equals("InsertError") == true)
            return updateStatusMessage;
        if(key.equals("SearchError") == true)
            return updateStatusMessage;


    */

        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);


    }
    //-----------------------------------------------------------------------------------

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
   /* public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }
    */

    //-----------------------------------------------------------------------------------
    //Save the Scout into the database
    public void save() {

        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("scoutId") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("scoutId",
                        persistentState.getProperty("scoutId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Scout information for scoutId: " + persistentState.getProperty("scoutId") + " updated successfully in database!";
            } else {
                Integer scoutId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("scoutId", "" + scoutId.intValue());
                updateStatusMessage = "Scout data for new Scout: " + persistentState.getProperty("scoutId")
                        + " added successfully to database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding scout data to database!";
        }
        System.out.println(updateStatusMessage + "\n");
    }
    //-----------------------------------------------------------------------------------
    //Compare two scoutss in the database by scoutID
    public static int compare(Scout a, Scout b) {
        String aNum = (String) a.getState("scoutId");
        String bNum = (String) b.getState("scoutId");

        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("scoutId"));
        v.addElement(persistentState.getProperty("firstName"));
        v.addElement(persistentState.getProperty("middleName"));
        v.addElement(persistentState.getProperty("lastName"));
        v.addElement(persistentState.getProperty("dateOfBirth"));
        v.addElement(persistentState.getProperty("phoneNumber"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("troopId"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));




        return v;
    }
    //-----------------------------------------------------------------------------------
    public String toString(){
        return("Scout Id: " +   persistentState.getProperty("scoutId")+
               "First Name: " + persistentState.getProperty("firstName") +
                "Middle Name: " + persistentState.getProperty("middleName") +
                "Last Name: " +  persistentState.getProperty("lastName") +
                "Date Of Birth: " + persistentState.getProperty("dateOfBirth") +
                "Phone Number: " + persistentState.getProperty("phoneNumber") +
                "Email: " + persistentState.getProperty("email") +
                "Status: " + persistentState.getProperty("status") +
                "Date Status Updated: " + persistentState.getProperty("dateStatusUpdated") +
                "TroopID: " + persistentState.getProperty("troopId"));

    }

}