package model;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

public class TreeSaleCollection extends EntityBase {

    private static final String myTableName = "Transaction";
    private Vector<TreeSale> treeSales;

    //Constructor
    public TreeSaleCollection()
    {
        super(myTableName);
        treeSales = new Vector();
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TreeSales"))
            return treeSales;
        else if (key.equals("TreeSaleList"))
            return this;
        else if(key.equals("Count"))
            return treeSales.size();
        return null;
    }
    //-----------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }
    //-----------------------------------------------------------------------------------
    private void addTreeSale(TreeSale a) {
        int index = findIndexToAdd(a);
        treeSales.insertElementAt(a, index);
    }
    //----------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------

    private Vector TreeSaleHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            treeSales = new Vector<TreeSale>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextTreeSaleData = (Properties) allDataRetrieved.elementAt(count);

               TreeSale treeSale = new TreeSale(nextTreeSaleData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (treeSale != null) {
                    addTreeSale(treeSale);
                }


            }
        }
        return treeSales;
    }
    //----------------------------------------------------------------------------------
    public Vector findTotalCash(String sessionId){
        String query = "SELECT * FROM " + myTableName + " WHERE sessionId=" + sessionId;
        return TreeSaleHelper(query);
    }


    //Find where to put new tree into Tree Vector
    private int findIndexToAdd(TreeSale a) {
        //users.add(u);
        int low = 0;
        int high = treeSales.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            TreeSale midTreeSale = treeSales.elementAt(middle);
            //compares the accounts to help with the sort binary search
            int result = TreeSale.compare(a, midTreeSale);

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