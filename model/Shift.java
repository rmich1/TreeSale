package model;

import exception.InvalidPrimaryKeyException;
import model.EntityBase;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Shift extends EntityBase {
    //Table Name Scout
    private static final String myTableName = "Shift";

    // GUI Components

    private String updateStatusMessage = "";

    protected Properties dependencies;
    //Empty Constructor
    public Shift(){
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }
    //Looks for a scoutID this will never be used
    public Shift(String shiftId)
            throws InvalidPrimaryKeyException {
        //calling the constructor to the EntityBase by passing in the name of the table
        super(myTableName);
        setDependencies();
        //SQL Query to get all scouts with scoutID's
        String query = "SELECT * FROM " + myTableName + " WHERE (shiftId = " + shiftId + ")";

        //creates a vector of all objects where the information from the table is related to the primary key
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one scout
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple shifts matching id : "
                        + shiftId + " found.");
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
            throw new InvalidPrimaryKeyException("No shifts matching id : "
                    + shiftId + " found.");
        }

    }
    //This is to add something into the database that doesn't exist
    public Shift(Properties props) {
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
        myRegistry.setDependencies(dependencies);
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {


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


    //-----------------------------------------------------------------------------------
    //Save the Scout into the database
    public void save() {

        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("shiftId") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("shiftId",
                        persistentState.getProperty("shiftId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Shift information for shiftId: " + persistentState.getProperty("shiftId") + " updated successfully in database!";
            } else {
                Integer shiftId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("shiftId", "" + shiftId.intValue());
                updateStatusMessage = "Shift data for new Shift: " + persistentState.getProperty("shiftId")
                        + " added successfully to database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding shift data to database!";
        }
        System.out.println(updateStatusMessage + "\n");
    }
    //-----------------------------------------------------------------------------------
    //Compare two scoutss in the database by scoutID
    public static int compare(Shift a, Shift b) {
        String aNum = (String) a.getState("shiftId");
        String bNum = (String) b.getState("shiftId");

        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("shiftId"));
        v.addElement(persistentState.getProperty("sessionId"));
        v.addElement(persistentState.getProperty("scoutId"));
        v.addElement(persistentState.getProperty("startTime"));
        v.addElement(persistentState.getProperty("companionName"));
        v.addElement(persistentState.getProperty("endTime"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }
    //-----------------------------------------------------------------------------------
    public String toString(){
        return("Shift Id: " +   persistentState.getProperty("shiftId")+
                "Session Id: " + persistentState.getProperty("sessionId") +
                "Scout Id: " + persistentState.getProperty("scoutId") +
                "Start Time: " +  persistentState.getProperty("startTime") +
                "Companion Name: " + persistentState.getProperty("companionName") +
                "End Time: " + persistentState.getProperty("endTime") +
                "Status: " + persistentState.getProperty("status"));

    }

}