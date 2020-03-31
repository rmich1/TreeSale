package model;

import exception.InvalidPrimaryKeyException;
import model.EntityBase;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Session extends EntityBase {
    //Table Name Scout
    private static final String myTableName = "Session";

    // GUI Components

    private String updateStatusMessage = "";

    protected Properties dependencies;
    //Empty Constructor
    public Session(){
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }
    //Looks for a scoutID this will never be used
    public Session(String sessionId)
            throws InvalidPrimaryKeyException {
        //calling the constructor to the EntityBase by passing in the name of the table
        super(myTableName);
        setDependencies();
        //SQL Query to get all scouts with scoutID's
        String query = "SELECT * FROM " + myTableName + " WHERE (sessionId = " + sessionId + ")";

        //creates a vector of all objects where the information from the table is related to the primary key
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one scout
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple shifts matching id : "
                        + sessionId + " found.");
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
            throw new InvalidPrimaryKeyException("No sessions matching id : "
                    + sessionId + " found.");
        }

    }
    //This is to add something into the database that doesn't exist
    public Session(Properties props) {
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
            if (persistentState.getProperty("sessionId") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("sessionId",
                        persistentState.getProperty("sessionId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Session information for sessionId: " + persistentState.getProperty("sessionId") + " updated successfully in database!";
            } else {
                Integer sessionId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("sessionId", "" + sessionId.intValue());
                updateStatusMessage = "Session data for new Session: " + persistentState.getProperty("sessionId")
                        + " added successfully to database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding shift data to database!";
        }
        System.out.println(updateStatusMessage + "\n");
    }
    //-----------------------------------------------------------------------------------
    //Compare two scoutss in the database by scoutID
    public static int compare(Session a, Session b) {
        String aNum = (String) a.getState("sessionId");
        String bNum = (String) b.getState("sessionId");

        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("sessionId"));
        v.addElement(persistentState.getProperty("startTime"));
        v.addElement(persistentState.getProperty("endTime"));
        v.addElement(persistentState.getProperty("startingCash"));
        v.addElement(persistentState.getProperty("endingCash"));
        v.addElement(persistentState.getProperty("totalCheckTransactionAmount"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }
    //-----------------------------------------------------------------------------------
    public String toString(){
        return( "Session Id: " + persistentState.getProperty("sessionId") +
                "Start Time: " +  persistentState.getProperty("startTime") +
                "End Time: " + persistentState.getProperty("endTime") +
                "Total Check Transaction Amount: " + persistentState.getProperty("totalCheckTransactionAmount") +
                "Status: " + persistentState.getProperty("status"));

    }




}