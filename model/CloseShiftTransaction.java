package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class CloseShiftTransaction extends Transaction {



    //Constructor
    protected CloseShiftTransaction()  {
    }
    //----------------------------------------------------------



    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CloseShift", "CloseShiftError");

        myRegistry.setDependencies(dependencies);
    }
    //----------------------------------------------------------
    public Object getState(String key) {

        return null;
    }

    public void stateChangeRequest(String key, Object value) {


        myRegistry.updateSubscribers(key, this);

    }

    protected Scene createView() {
        Scene scene = myViews.get("CloseShiftView");

        if (scene == null) {
            View view = ViewFactory.createView("CloseShiftView", this);
            scene = new Scene(view);
            myViews.put("CloseShiftView", scene);
        }

        return scene;
    }

    private void createAndShowTreeCollectionView() {

    }

}