package model;

import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;


public class ScoutTransaction extends Transaction {
    private Scout scout = new Scout((Properties) null);
    private String submissionResponse = "";
    private Boolean error = false;


    public ScoutTransaction(Scout sct){
        super();

        if (sct != null) {
            scout = sct;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SubmitNewScout", "SubmissionResponse");

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
            case "SubmissionResponse":
                return new Pair<>((String) scout.getState("scoutId"),
                        new Pair<>(submissionResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

       if(key.equals("SubmitNewScout")){
           scout = new Scout((Properties) value);
           scout.save();
           error = false;
           submissionResponse = "Scout Added Successfully!";
        }

        myRegistry.updateSubscribers(key, this);
    }
    //Creating the addScout View
    protected Scene createView() {
        Scene scene = myViews.get("AddScout");

        if (scene == null) {
            View view = ViewFactory.createView("AddScout", this);
            scene = new Scene(view);
            myViews.put("AddScout", scene);
        }

        return scene;
    }

}
