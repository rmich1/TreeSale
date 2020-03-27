package model;

import database.Persistable;
import exception.InvalidPrimaryKeyException;
import model.EntityBase;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class TreeType extends EntityBase {

    private static final String myTableName = "TreeType";

    // GUI Components

    private String updateStatusMessage = "";

    protected Properties dependencies;


    //Empty Constructor
    public TreeType() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    public TreeType(String barcodePrefix)
            throws InvalidPrimaryKeyException {
        //calling the constructor to the EntityBase by passing in the name of the table
        super(myTableName);
        setDependencies();
        //SQL Query to get all trees with barcodes's
        String query = "SELECT * FROM " + myTableName + " WHERE (barcodePrefix = \"" + barcodePrefix + "\")";

        //creates a vector of all objects where the information from the table is related to the primary key
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one barcode
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple trees matching barcode : "
                        + barcodePrefix + " found.");
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
                    + barcodePrefix + " found.");
        }

    }

    //This is to add something into the database that doesn't exist
    public TreeType(Properties props) {
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

        insertStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void insertStateInDatabase() {
        try {
            if (persistentState.getProperty("barcodePrefix") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcodePrefix",
                        persistentState.getProperty("barcodePrefix"));
                whereClause.setProperty("typeDesc", persistentState.getProperty("typeDesc"));
                whereClause.setProperty("cost", persistentState.getProperty("cost"));
                insertPersistentState(mySchema,whereClause);

                updateStatusMessage = "Tree information for barcode prefix: " + persistentState.getProperty("barcodePrefix") + " updated successfully in database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding tree data to database!";
        }
        System.out.println(updateStatusMessage + "\n");
    }
    public void updateStateInDatabase(){
        try{
            if(persistentState.getProperty("barcodePrefix") != null){
                Properties updateValues = new Properties();
                Properties whereValues = new Properties();
                whereValues.setProperty("barcodePrefix", persistentState.getProperty("barcodePrefix"));

                updateValues.setProperty("typeDesc", persistentState.getProperty("typeDesc"));
                updateValues.setProperty("cost", persistentState.getProperty("cost"));
                updatePersistentState(mySchema, updateValues, whereValues);


            }
        } catch (SQLException e) {
            updateStatusMessage = "Error in updating tree data to database!";
        }
    }

    public void deleteInDatabase(){
        try{
            if(persistentState.getProperty("barcodePrefix") != null){
                Properties whereValues = new Properties();
                whereValues.setProperty("barcodePrefix", persistentState.getProperty("barcodePrefix"));

                deletePersistentState(mySchema, whereValues);


            }
        } catch (SQLException e) {
            updateStatusMessage = "Error in updating tree data to database!";
        }

    }

    //-----------------------------------------------------------------------------------
    //Compare two trees in the database by barcode
    public static int compare(TreeType a, TreeType b) {
        String aNum = (String) a.getState("barcodePrefix");
        String bNum = (String) b.getState("barcodePrefix");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("barcodePrefix"));
        v.addElement(persistentState.getProperty("typeDesc"));
        v.addElement(persistentState.getProperty("cost"));


        return v;
    }

    //-----------------------------------------------------------------------------------
    public String toString() {
        return("Barcode Prefix: " + persistentState.getProperty("barcodePrefix") +
                "Type Description: " + persistentState.getProperty("typeDesc") +
                "Cost: $" + persistentState.getProperty("cost"));


    }
}