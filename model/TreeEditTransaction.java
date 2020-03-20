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


public class TreeEditTransaction extends Transaction {
    private Tree tree;
    private TreeCollection treeCol;
    private String EditResponse = "";
    private String DeleteResponse= "";
    private String EditBarcodeResponse="";
    private Boolean error = false;



    public TreeEditTransaction(Tree tr){
        super();

        if (tr != null) {
            tree = tr;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("EditTree", "EditResponse");
        dependencies.setProperty("EditBarcode", "EditBarcodeResponse");
       dependencies.setProperty("DeleteScout", "DeleteResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "barcode":
            case "treeType":
            case "status":
            case "dateStatusUpdated":
            case "Notes":

                return tree.getState(key);
            case "EditResponse":
                return new Pair<>((String) tree.getState("barcode"),
                        new Pair<>(EditResponse, error));
            case "DeleteResponse":
                return new Pair<>((String) tree.getState("barcode"),
                        new Pair<>(DeleteResponse, error));
            case "EditBarcodeResponse":
                return new Pair<>((String) tree.getState("barcode"),
                        new Pair<>(EditBarcodeResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if(key.equals("EditTree")){
            tree = new Tree((Properties) value);
            tree.updateStateInDatabase();
            error = false;
            EditResponse = "Tree Updated Successfully!";



        }
        else if(key.equals("EditBarcode")){
            tree = new Tree((Properties) value);
            tree.save();
            error = false;
            EditBarcodeResponse = "Tree Updated Successfully ";

        }
        else if(key.equals("DeleteScout")){
        error = false;
        DeleteResponse = "Tree Deleted Successfully!";

        }

        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView() {
        Scene scene = myViews.get("EditTreeInfo");

        if (scene == null) {
            View view = ViewFactory.createView("EditTreeInfo", this);
            scene = new Scene(view);
            myViews.put("EditTreeInfo", scene);
        }

        return scene;
    }

}
