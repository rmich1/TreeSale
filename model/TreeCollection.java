package model;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

public class TreeCollection extends EntityBase {

    private static final String myTableName = "Tree";
    private Vector<Tree> trees;

    //Constructor
    public TreeCollection()
    {
        super(myTableName);
        trees = new Vector();
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("Trees"))
            return trees;
        else if (key.equals("TreeList"))
            return this;
        else if(key.equals("Count"))
            return trees.size();
        return null;
    }
    //-----------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }
    //-----------------------------------------------------------------------------------
    private void addTree(Tree a) {
        int index = findIndexToAdd(a);
        trees.insertElementAt(a, index);
    }
    //----------------------------------------------------------------------------------
    public void findTreeBarcode(String barcode){
            String query = "SELECT * FROM " + myTableName + " WHERE barcode = \"" + barcode + "\"";
            treeHelper(query);


        }
    //----------------------------------------------------------------------------------

    private void treeHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            trees = new Vector<Tree>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextTreeData = (Properties) allDataRetrieved.elementAt(count);

                Tree tree = new Tree(nextTreeData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (tree != null) {
                    addTree(tree);
                }


            }
        }
    }
    //----------------------------------------------------------------------------------


    //Find where to put new tree into Tree Vector
    private int findIndexToAdd(Tree a) {
        //users.add(u);
        int low = 0;
        int high = trees.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            Tree midSession = trees.elementAt(middle);
            //compares the accounts to help with the sort binary search
            int result = Tree.compare(a, midSession);

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
            trees = new Vector<Tree>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextTreeData = (Properties) allDataRetrieved.elementAt(count);

                Tree tree = new Tree(nextTreeData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (tree != null) {
                    addTree(tree);
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