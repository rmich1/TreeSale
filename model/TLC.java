package model;

import event.Event;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.security.jgss.spi.GSSContextSpi;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;

public class TLC implements IView, IModel {
    // For Impresario
    private Properties dependencies;
    private ModelRegistry registry;

    // GUI Components
    private Hashtable<String, Scene> views;
    private Stage stage;
    private String errMsg;

    public TLC() {
        stage = MainStageContainer.getInstance();
        views = new Hashtable<>();

        registry = new ModelRegistry("TLC");

        createAndShowView("TLCView");
    }

    public Object getState(String key) {
        return null;
    }

    public void subscribe(String key, IView subscriber) {
        registry.subscribe(key, subscriber);
    }

    public void unSubscribe(String key, IView subscriber) {
        registry.unSubscribe(key, subscriber);
    }

    public void stateChangeRequest(String key, Object value) {
        switch (key) {


            case "Done":
                Platform.exit();
                break;

            case "ScoutSelected":
            case "NewScout":
            case "SearchScout":
            case "TreeSelected":
            case "NewTree":
            case "SearchTree":
            case "NewTreeType":
            case "SearchTreeType":
            case "TreeTypeSelected":
            case "NewSession":
                initiateTransaction(key, value);
                break;
            case "Return":
                createAndShowView("TLCView");
                break;



            default:
                break;
        }

        registry.updateSubscribers(key, this);
    }

    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    public void initiateTransaction(String delegateLabel, Object resource) {
        Transaction delegate = TransactionFactory.createTransaction(delegateLabel, resource);
        if (delegate != null) {
            delegate.subscribe("Return", this);
            delegate.subscribe("ScoutSelected", this);
            delegate.subscribe("SearchScout", this);
            delegate.subscribe("TreeSelected", this);
            delegate.subscribe("SearchTree", this);
            delegate.subscribe("TreeTypeSelected", this);
            delegate.doYourJob();
        }
    }


    public void swapToView(Scene scene) {
        if (scene == null) {
            System.out.println("Librarian.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
        } else {
            stage.setScene(scene);
            stage.sizeToScene();
            WindowPosition.placeCenter(stage);
        }
    }

    private void createAndShowView(String viewName) {
        Scene scene = views.get(viewName);

        if (scene == null) {
            View view = ViewFactory.createView(viewName, this);
            scene = new Scene(view);
            views.put(viewName, scene);
        }

        swapToView(scene);
    }



}
