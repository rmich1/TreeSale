package model;


import exception.InvalidPrimaryKeyException;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;


public class ScoutEditTransaction extends Transaction {
    private Scout scout;
    private ScoutCollection scoutCol;
    private String EditResponse = "";
    private String DeleteResponse= "";
    private Boolean error = false;



    public ScoutEditTransaction(Scout sct){
        super();

        if (sct != null) {
            scout = sct;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("EditScout", "EditResponse");
        dependencies.setProperty("DeleteScout", "DeleteResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "scoutId":
            case "firstName":
            case "middleName":
            case "lastName":
            case "dateOfBirth":
            case "phoneNumber":
            case "email":
            case "status":
            case "dateStatusUpdated":
            case "troopId":
                return scout.getState(key);
            case "EditResponse":
                return new Pair<>((String) scout.getState("scoutId"),
                        new Pair<>(EditResponse, error));
            case "DeleteResponse":
                return new Pair<>((String) scout.getState("scoutId"),
                        new Pair<>(DeleteResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if(key.equals("EditScout")){
            scout = new Scout((Properties) value);
            scout.save();
            error = false;
            EditResponse = "Scout Updated successfully!";



        }
        else if(key.equals("DeleteScout")){
            scout = new Scout((Properties) value);
            scout.save();
            error = false;
            DeleteResponse = "Scout Deleted";
        }

        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView() {
        Scene scene = myViews.get("EditScoutInfo");

        if (scene == null) {
            View view = ViewFactory.createView("EditScoutInfo", this);
            scene = new Scene(view);
            myViews.put("EditScoutInfo", scene);
        }

        return scene;
    }

}
