package model;
import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

public class SessionCollection extends EntityBase {

    private static final String myTableName = "Session";
    private Vector<Session> sessions;

    //Constructor
    public SessionCollection()
    {
        super(myTableName);
        sessions = new Vector();
    }
    //-----------------------------------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("Sessions"))
            return sessions;
        else if (key.equals("SessionList"))
            return this;
        else if(key.equals("Count"))
            return sessions.size();
        return null;
    }
    //-----------------------------------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);
    }
    //-----------------------------------------------------------------------------------
    private void addSession(Session a) {
        int index = findIndexToAdd(a);
        sessions.insertElementAt(a, index);
    }
    //----------------------------------------------------------------------------------
    public Boolean isOpenSessions(){
        Boolean open = false;
        Vector<Session> sessionVector = new Vector();
        String query = "SELECT * FROM " + myTableName;
        sessionVector = sessionHelper(query);
        for(int i = 0; i < sessionVector.size(); i++){
            if(sessionVector.get(i).persistentState.getProperty("status").equals("Active")){
                open = true;
            }
        }
        return open;

    }
    public Vector<Session> findOpenSessions(){
        Vector<Session> sessionVector = new Vector();
        String query = "SELECT * FROM " + myTableName + " WHERE status='Active'";
        sessionVector = sessionHelper(query);
        return sessionVector;
    }

    //----------------------------------------------------------------------------------

    private Vector sessionHelper(String query){
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            sessions = new Vector<Session>();
            //populates the books vector
            for (int count = 0; count < allDataRetrieved.size(); count++) {
                Properties nextSessionData = (Properties) allDataRetrieved.elementAt(count);

                Session session = new Session(nextSessionData);
                //Adding each account to a collection(accounts vector)
                //Have a seperate method to add account because the method will build up a sorted account collection
                if (session != null) {
                    addSession(session);
                }


            }
        }
        return sessions;
    }
    //----------------------------------------------------------------------------------


    //Find where to put new tree into Tree Vector
    private int findIndexToAdd(Session a) {
        //users.add(u);
        int low = 0;
        int high = sessions.size() - 1;
        int middle;

        while (low <= high) {
            middle = (low + high) / 2;

            Session midSession = sessions.elementAt(middle);
            //compares the accounts to help with the sort binary search
            int result = Session.compare(a, midSession);

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