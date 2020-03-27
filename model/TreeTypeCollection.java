package model;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

public class TreeTypeCollection extends EntityBase {

    private static final String myTableName = "TreeType";
    private Vector<TreeType> treeTypes;

    //Constructor
    public TreeTypeCollection()
    {
        super(myTableName);
        treeTypes = new Vector();
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TreeTypes"))
            return treeTypes;
        else if (key.equals("TreeTypeList"))
            return this;
        else if(key.equals("Count"))
            return treeTypes.size();
        return null;
    }
    //-----------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }
    //-----------------------------------------------------------------------------------
    private void addTree(TreeType a) {
        int index = findIndexToAdd(a);
        treeTypes.insertElementAt(a, index);
    }
    //----------------------------------------------------------------------------------
    public Vector findTreeBarcodePrefix(String barcodePrefix){
        Vector barcodeArray = new Vector();
        String query = "SELECT * FROM " + myTableName + " WHERE barcodePrefix = \"" + barcodePrefix + "\"";
        return barcodeArray = treeHelper(query);

    }
    //----------------------------------------------------------------------------------

    private Vector treeHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            treeTypes = new Vector<TreeType>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextTreeTypeData = (Properties) allDataRetrieved.elementAt(count);

                TreeType treeType = new TreeType(nextTreeTypeData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (treeType != null) {
                    addTree(treeType);
                }


            }
        }
        return allDataRetrieved;
    }
    //----------------------------------------------------------------------------------

    public boolean isDuplicate(String barcodePrefix){
        Vector<Tree> barcodePrefixVector = new Vector<>();
        barcodePrefixVector = findTreeBarcodePrefix(barcodePrefix);

        if(barcodePrefixVector.size() == 1){
            return true;

        }
        else {
            return false;
        }

    }
    //Find where to put new tree into Tree Vector
    private int findIndexToAdd(TreeType a) {
        //users.add(u);
        int low = 0;
        int high = treeTypes.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            TreeType midSession = treeTypes.elementAt(middle);
            //compares the accounts to help with the sort binary search
            int result = TreeType.compare(a, midSession);

            if (result == 0) {
                return middle;
            } else if (result < 0) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return low;
    }

    //----------------------------------------------------------------------------------

    //Populates the Scout Vector
    private void TreeHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            //initilizing account vector, get a new set of account objects to populate
            treeTypes = new Vector<TreeType>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextTreeTypeData = (Properties) allDataRetrieved.elementAt(count);

                TreeType treeType = new TreeType(nextTreeTypeData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (treeType != null) {
                    addTree(treeType);
                }


            }
        }
    }
    //----------------------------------------------------------------------------------

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    //-----------------------------------------------------------------------------------


    //Every class must have this it has a schema to do the updates
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }

}