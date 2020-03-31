package model;

import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;
import impresario.*;


public class SessionTransaction extends Transaction{
    private Session session = new Session((Properties) null);
    private Shift shift = new Shift((Properties) null);
    private String sessionResponse = "";
    private Boolean error = false;
    private ModelRegistry myModel;
    private IModel imodel;


    public SessionTransaction(Session sess){
        super();

        if (sess != null) {
            session = sess;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("OpenNewSession", "OpenResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "sessionId":
            case "startTime":
            case "endTime":
            case "startingCash":
            case "endingCash":
            case "status":
                return session.getState(key);
            case "OpenResponse":
                return new Pair<>((String) session.getState("sessionId"),
                        new Pair<>(sessionResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if(key.equals("OpenNewSession")){
            session = new Session((Properties) value);
            session.save();
            error = false;
            sessionResponse = "Session Opened Successfully!";
            createAndShowOpenShift();

        }

        myRegistry.updateSubscribers(key, this);

    }
    //Creating the addScout View
    protected Scene createView() {
        Scene scene = myViews.get("OpenSession");

        if (scene == null) {
            View view = ViewFactory.createView("OpenSession", this);
            scene = new Scene(view);
            myViews.put("OpenSession", scene);
        }

        return scene;
    }
    public void createAndShowOpenShift(){
        Scene scene = myViews.get("OpenShift");

        if (scene == null) {
            View view = ViewFactory.createView("OpenShift", this);
            scene = new Scene(view);
            myViews.put("OpenShift", scene);
        }

        swapToView(scene);
    }
    }





