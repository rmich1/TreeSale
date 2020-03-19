package model;

import exception.InvalidPrimaryKeyException;
import model.EntityBase;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Tree extends EntityBase {

    private static final String myTableName = "Tree";

    // GUI Components

    private String updateStatusMessage = "";

    protected Properties dependencies;

    //Empty Constructor
    public Tree() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    public Tree(String barcode)
            throws InvalidPrimaryKeyException {
        //calling the constructor to the EntityBase by passing in the name of the table
        super(myTableName);
        setDependencies();
        //SQL Query to get all trees with barcodes's
        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = \"" + barcode + "\")";

        //creates a vector of all objects where the information from the table is related to the primary key
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one barcode
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple trees matching barcode : "
                        + barcode + " found.");
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
            throw new InvalidPrimaryKeyException("No trees matching barcode : "
                    + barcode + " found.");
        }

    }

    //This is to add something into the database that doesn't exist
    public Tree(Properties props) {
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
            if (persistentState.getProperty("barcode") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode",
                        persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree information for barcode: " + persistentState.getProperty("barcode") + " updated successfully in database!";
            } else {
                Integer barcode =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("barcode", "" + barcode.intValue());
                updateStatusMessage = "Tree data for new Tree: " + persistentState.getProperty("barcode")
                        + " added successfully to database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding tree data to database!";
        }
        System.out.println(updateStatusMessage + "\n");
    }

    //-----------------------------------------------------------------------------------
    //Compare two trees in the database by barcode
    public static int compare(Tree a, Tree b) {
        String aNum = (String) a.getState("barcode");
        String bNum = (String) b.getState("barcode");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("treeType"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));
        v.addElement(persistentState.getProperty("notes"));


        return v;
    }

    //-----------------------------------------------------------------------------------
    public String toString() {
        return("Barcode: " + persistentState.getProperty("barcode") +
                "Tree Type: " + persistentState.getProperty("treeType") +
                "Status: " + persistentState.getProperty("status") +
                "Notes: " + persistentState.getProperty("notes"));


    }
}