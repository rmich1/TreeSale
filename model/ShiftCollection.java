package model;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

public class ShiftCollection extends EntityBase {

    private static final String myTableName = "Shift";
    private Vector<Shift> shifts;

    //Constructor
    public ShiftCollection()
    {
        super(myTableName);
        shifts = new Vector();
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("Shifts"))
            return shifts;
        else if (key.equals("ShiftList"))
            return this;
        else if(key.equals("Count"))
            return shifts.size();
        return null;
    }
    //-----------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }
    //-----------------------------------------------------------------------------------
    private void addShift(Shift a) {
        int index = findIndexToAdd(a);
        shifts.insertElementAt(a, index);
    }
    public Vector<Shift> findOpenShift(){
        Vector<Shift> shiftVector = new Vector();
        String query = "SELECT * FROM " + myTableName + " WHERE status='Active'";
       return shiftVector = shiftHelper(query);
    }
    //----------------------------------------------------------------------------------
    public Boolean isOpenShift(){
        Boolean openShift = false;
        Vector<Shift> shiftVector = new Vector();
        String query = "SELECT * FROM " + myTableName;
        shiftVector = shiftHelper(query);
        for(int i = 0; i < shiftVector.size(); i++){
            if(shiftVector.get(i).persistentState.getProperty("status").equals("Active")){
                openShift = true;
            }
        }
        return openShift;

    }
    //----------------------------------------------------------------------------------

    private Vector shiftHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            shifts = new Vector<Shift>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextShiftData = (Properties) allDataRetrieved.elementAt(count);

                Shift shift = new Shift(nextShiftData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (shift != null) {
                    addShift(shift);
                }


            }
        }
        return shifts;
    }
    //----------------------------------------------------------------------------------


    //Find where to put new tree into Tree Vector
    private int findIndexToAdd(Shift a) {
        //users.add(u);
        int low = 0;
        int high = shifts.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            Shift midSession = shifts.elementAt(middle);
            //compares the accounts to help with the sort binary search
            int result = Shift.compare(a, midSession);

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