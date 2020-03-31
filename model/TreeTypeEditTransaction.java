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


public class TreeTypeEditTransaction extends Transaction {
    private TreeType treeType;
    private TreeTypeCollection treeTypeCol;
    private String EditResponse = "";
    private String DeleteResponse= "";
    private String EditBarcodePrefixResponse="";
    private Boolean error = false;



    public TreeTypeEditTransaction(TreeType tt){
        super();

        if (tt != null) {
            treeType = tt;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("EditTreeType", "EditResponse");
        dependencies.setProperty("EditBarcodePrefix", "EditBarcodePrefixResponse");
        dependencies.setProperty("DeleteTreeType", "DeleteResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "barcodePrefix":
            case "typeDesc":
            case "cost":

                return treeType.getState(key);
            case "EditResponse":
                return new Pair<>((String) treeType.getState("barcodePrefix"),
                        new Pair<>(EditResponse, error));
            case "DeleteResponse":
                return new Pair<>((String) treeType.getState("barcodePrefix"),
                        new Pair<>(DeleteResponse, error));
            case "EditBarcodePrefixResponse":
                return new Pair<>((String) treeType.getState("barcodePrefix"),
                        new Pair<>(EditBarcodePrefixResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if(key.equals("EditTreeType")){
            treeType = new TreeType((Properties) value);
            treeType.updateStateInDatabase();
            error = false;
            EditResponse = "Tree Type Updated Successfully!";



        }
        else if(key.equals("EditBarcodePrefix")){
            treeType= new TreeType((Properties) value);
            treeType.save();
            error = false;
            EditBarcodePrefixResponse = "Tree Type Updated Successfully ";

        }
        else if(key.equals("DeleteTreeType")){
            error = false;
            DeleteResponse = "Tree Type Deleted Successfully!";

        }

        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView() {
        Scene scene = myViews.get("EditTreeTypeInfo");

        if (scene == null) {
            View view = ViewFactory.createView("EditTreeTypeInfo", this);
            scene = new Scene(view);
            myViews.put("EditTreeTypeInfo", scene);
        }

        return scene;
    }

}
