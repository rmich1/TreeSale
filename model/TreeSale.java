package model;

import exception.InvalidPrimaryKeyException;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class TreeSale extends EntityBase {
    //Table Name Scout
    private static final String myTableName = "Transaction";

    // GUI Components

    private String updateStatusMessage = "";

    protected Properties dependencies;
    //Empty Constructor
    public TreeSale(){
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }
    //Looks for a TransactionId
    public TreeSale(String transactionId)
            throws InvalidPrimaryKeyException {
        //calling the constructor to the EntityBase by passing in the name of the table
        super(myTableName);
        setDependencies();
        //SQL Query to get all scouts with scoutID's
        String query = "SELECT * FROM " + myTableName + " WHERE (transactionId = " + transactionId + ")";

        //creates a vector of all objects where the information from the table is related to the primary key
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one scout
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple transactionid matching id : "
                        + transactionId + " found.");
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
            throw new InvalidPrimaryKeyException("No transactions matching id : "
                    + transactionId + " found.");
        }

    }
    //This is to add something into the database that doesn't exist
    public TreeSale(Properties props) {
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
            if (persistentState.getProperty("transactionId") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("transactionId",
                        persistentState.getProperty("transactionId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Transaction information for transactionId: " + persistentState.getProperty("transactionId") + " updated successfully in database!";
            } else {
                Integer transactionId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("transactionId", "" + transactionId.intValue());
                updateStatusMessage = "Transaction data for new Transaction: " + persistentState.getProperty("transactionId")
                        + " added successfully to database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in adding transaction data to database!";
        }
        System.out.println(updateStatusMessage + "\n");
    }
    //-----------------------------------------------------------------------------------
    //Compare two transaction in the database by transactionID
    public static int compare(TreeSale a, TreeSale b) {
        String aNum = (String) a.getState("transactionId");
        String bNum = (String) b.getState("transactionId");

        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("transactionId"));
        v.addElement(persistentState.getProperty("transactionType"));
        v.addElement(persistentState.getProperty("sessionId"));
        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("barcodePrefix"));
        v.addElement(persistentState.getProperty("cost"));
        v.addElement(persistentState.getProperty("paymentType"));
        v.addElement(persistentState.getProperty("custName"));
        v.addElement(persistentState.getProperty("custPhone"));
        v.addElement(persistentState.getProperty("custEmail"));

        return v;
    }
    public String getStartingCash(){
        return persistentState.getProperty("startingCash").toString();
    }
    //-----------------------------------------------------------------------------------
    public String toString(){
        return("Transaction ID :" + persistentState.getProperty("transactionId")
        + " Transaction Type: " + persistentState.getProperty("transactionType")
    + " Session ID: " + persistentState.getProperty("sessionId") + " Barcode: " +
                persistentState.getProperty("barcode") + " Barcode Prefix: " + persistentState.getProperty("barcodePrefix")
        + " Cost: " + persistentState.getProperty("cost") + " Payment Type: " +
                persistentState.getProperty("paymentType") + " Customer Name: " +
                persistentState.getProperty("custName") + " Customer Phone: " +
                persistentState.getProperty("custPhone") + " Customer Email: " +
                persistentState.getProperty("custEmail"));

    }




}