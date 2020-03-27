package model;

import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;


public class TreeTypeTransaction extends Transaction {
    private TreeType treeType = new TreeType();
    private String submissionResponse = "";
    private Boolean error = false;


    public TreeTypeTransaction(TreeType tt){
        super();

        if (tt != null) {
            treeType = tt;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SubmitNewTreeType", "SubmissionResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "barcodePrefix":
            case "treeDesc":
            case "cost":
                return treeType.getState(key);
            case "SubmissionResponse":
                return new Pair<>((String) treeType.getState("barcodePrefix"),
                        new Pair<>(submissionResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if(key.equals("SubmitNewTreeType")){
            treeType = new TreeType((Properties) value);
            treeType.save();
            error = false;
            submissionResponse = "Tree Added Successfully!";
        }

        myRegistry.updateSubscribers(key, this);
    }
    //Creating the addTreeType View
    protected Scene createView() {
        Scene scene = myViews.get("AddTreeType");

        if (scene == null) {
            View view = ViewFactory.createView("AddTreeType", this);
            scene = new Scene(view);
            myViews.put("AddTreeType", scene);
        }

        return scene;
    }

}
