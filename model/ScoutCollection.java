package model;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

public class ScoutCollection extends EntityBase {

    private static final String myTableName = "Scout";
    private Vector<Scout> scouts;

    //Constructor
    public ScoutCollection()
    {
        super(myTableName);
        scouts = new Vector();
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("Scouts"))
            return scouts;
        else if (key.equals("ScoutList"))
            return this;
        else if(key.equals("Count"))
                return scouts.size();
        return null;
    }
    //-----------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }
    //-----------------------------------------------------------------------------------
    private void addScout(Scout a) {
        int index = findIndexToAdd(a);
        scouts.insertElementAt(a, index);
    }
    //----------------------------------------------------------------------------------
    //Populates the Scout Vector
    private Vector<Scout> scoutHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            //initilizing account vector, get a new set of account objects to populate
            scouts = new Vector<Scout>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextScoutData = (Properties) allDataRetrieved.elementAt(count);

                Scout scout = new Scout(nextScoutData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (scout != null) {
                    addScout(scout);
                }


            }
        }
        return scouts;
    }
    //----------------------------------------------------------------------------------
    //Finds all scouts with first name like AND Active and Inactive
    public void findScoutsWithFNameLike(String fName)
    {

        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + fName + "%'";
        scoutHelper(query);

    }
    //-----------------------------------------------------------------------------------
    //Finds all scouts with part of last name
    public void findScoutsWithLNameLike(String lName){
        String query = "SELECT * FROM " + myTableName + " WHERE lastName LIKE '%" + lName + "%'";
        scoutHelper(query);
    }
    //-----------------------------------------------------------------------------------
    //Finds all scouts with part of email
    public void findScoutWithEmailLike(String email){
        String query = "SELECT * FROM " + myTableName + " WHERE email LIKE '%" + email + "%'";
        scoutHelper(query);

    }
    //-----------------------------------------------------------------------------------
    //Find all scouts with part of first name and last name
    public Vector<Scout> findScoutswithFNameLNameLike(String[] fName){
        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + fName[0] + "%' AND lastName LIKE '%"
                + fName[1] + "%'";
            return scoutHelper(query);

    }
    //-----------------------------------------------------------------------------------
    //Find all scouts with part of first name, last name and email
    public void findScoutwithAllinfoLike(String[] info){
        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + info[0] + "%' AND lastName LIKE '%"
                + info[1] + "%' AND email LIKE '%" + info[2] + "%'";
        scoutHelper(query);

    }
    public void findScoutswithFNameEmailLike(String[] info){
        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + info[0] + "%' AND email LIKE '%"
                + info[1] + "%'";
        scoutHelper(query);
    }
    //----------------------------------------------------------------------------------
    public void findScoutswithLNameEmailLike(String[] info){
        String query = "SELECT * FROM " + myTableName + " WHERE lastName LIKE '%" + info[0] + "%' AND email LIKE '%"
                + info[1] + "%'";
        scoutHelper(query);
    }
    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------

    public Vector<Scout> findScoutsWithFNameLikeActive(String fName)
    {

        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + fName + "%' AND status='Active'";
        return scoutHelper(query);

    }
    //-----------------------------------------------------------------------------------
    //Finds all scouts with part of last name
    public void findScoutsWithLNameLikeActive(String lName){
        String query = "SELECT * FROM " + myTableName + " WHERE lastName LIKE '%" + lName + "%' AND status='Active'";
        scoutHelper(query);
    }
    //-----------------------------------------------------------------------------------
    //Finds all scouts with part of email
    public void findScoutWithEmailLikeActive(String email){
        String query = "SELECT * FROM " + myTableName + " WHERE email LIKE '%" + email + "%' AND status='Active'";
        scoutHelper(query);

    }
    //-----------------------------------------------------------------------------------
    //Find all scouts with part of first name and last name
    public Vector<Scout> findScoutswithFNameLNameLikeActive(String[] fName){

        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + fName[0] + "%' AND lastName LIKE '%"
                + fName[1] + "%' AND status='Active'";
        return scoutHelper(query);

    }
    //-----------------------------------------------------------------------------------
    //Find all scouts with part of first name, last name and email
    public void findScoutwithAllinfoLikeActive(String[] info){
        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + info[0] + "%' AND lastName LIKE '%"
                + info[1] + "%' AND email LIKE '%" + info[2] + "%' AND status='Active'";
        scoutHelper(query);

    }

    //-----------------------------------------------------------------------------------
    public void findScoutswithFNameEmailLikeActive(String[] info){
        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + info[0] + "%' AND email LIKE '%"
                + info[1] + "%' AND status='Active'";
        scoutHelper(query);
    }
    //----------------------------------------------------------------------------------
    public void findScoutswithLNameEmailLikeActive(String[] info){
        String query = "SELECT * FROM " + myTableName + " WHERE lastName LIKE '%" + info[0] + "%' AND email LIKE '%"
                + info[1] + "%' AND status='Active'";
        scoutHelper(query);
    }
    public Vector findAllActiveScouts(){
        Vector activeScouts = new Vector();
        String query = "SELECT firstName, lastName FROM " + myTableName + " WHERE status='Active'";
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            //initilizing account vector, get a new set of account objects to populate
            scouts = new Vector<Scout>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextScoutData = (Properties) allDataRetrieved.elementAt(count);

                Scout scout = new Scout(nextScoutData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (scout != null) {
                    scouts.addElement(scout);
                }


            }
        }
      return scouts;


    }
    //----------------------------------------------------------------------------------------
    //Find where to put new scout into Scout Vector
    private int findIndexToAdd(Scout a) {
        //users.add(u);
        int low = 0;
        int high = scouts.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            Scout midSession = scouts.elementAt(middle);
            //compares the accounts to help with the sort binary search
            int result = Scout.compare(a, midSession);

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


    //-----------------------------------------------------------------------------------
    public Scout retrieve(String scoutId) {
        Scout retValue = null;
        for (int cnt = 0; cnt < scouts.size(); cnt++) {
            Scout nextScout = scouts.elementAt(cnt);
            String nextScoutId = (String) nextScout.getState("scoutId");
            if (nextScoutId.equals(scoutId) == true) {
                retValue = nextScout;
                return retValue; // we should say 'break;' here
            }
        }
        return retValue;
    }
    //----------------------------------------------------------

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