package model;

import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;


public class ShiftTransaction extends Transaction {
    private Shift shift = new Shift((Properties) null);
    private String sessionResponse = "";
    private Boolean error = false;


    public ShiftTransaction(Shift sh){
        super();

        if (sh != null) {
            shift = sh;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("OpenNewShift", "OpenResponse");
        dependencies.setProperty("AddNewScoutToShift", "OpenResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "shiftId":
            case "sessionId":
            case "startTime":
            case "companionName":
            case "endTime":
            case "status":
                return shift.getState(key);
            case "OpenResponse":
                return new Pair<>((String) shift.getState("shiftId"),
                        new Pair<>(sessionResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if (key.equals("OpenNewShift")) {
            System.out.println("Here");
            shift = new Shift((Properties) value);
            shift.save();
            error = false;
            sessionResponse = "Shift Opened Successfully!";
        }


        if (key.equals("AddMoreScoutsToShift")) {
            shift = new Shift((Properties) value);
            shift.save();
            error = false;
            sessionResponse = "Shift Opened Successfully!";
            createandShowShiftView();
        }
        myRegistry.updateSubscribers(key, this);

    }
    //Creating the addScout View
    protected Scene createView() {
        Scene scene = myViews.get("OpenShift");

        if (scene == null) {
            View view = ViewFactory.createView("OpenShift", this);
            scene = new Scene(view);
            myViews.put("OpenShift", scene);
        }

        return scene;
    }
    public void createandShowShiftView(){
    Scene scene = myViews.get("OpenShift");

        if (scene == null) {
        View view = ViewFactory.createView("OpenShift", this);
        scene = new Scene(view);
        myViews.put("OpenShift", scene);
    }

    swapToView(scene);
}
}
