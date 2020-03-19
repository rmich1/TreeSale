package model;

import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;


public class TreeTransaction extends Transaction {
    private Tree tree = new Tree();
    private String submissionResponse = "";
    private Boolean error = false;


    public TreeTransaction(Tree tr){
        super();

        if (tr != null) {
            tree = tr;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SubmitNewTree", "SubmissionResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

        switch (key) {
            case "barcode":
            case "treeType":
            case "status":
            case "dateStatusUpdated":
            case "notes":
                return tree.getState(key);
            case "SubmissionResponse":
                return new Pair<>((String) tree.getState("barcode"),
                        new Pair<>(submissionResponse, error));
            default:
                return null;
        }
    }

    public void stateChangeRequest(String key, Object value) {

        if(key.equals("SubmitNewTree")){
            tree = new Tree((Properties) value);
            tree.save();
            error = false;
            submissionResponse = "Tree Added Successfully!";
        }

        myRegistry.updateSubscribers(key, this);
    }
    //Creating the addScout View
    protected Scene createView() {
        Scene scene = myViews.get("AddTree");

        if (scene == null) {
            View view = ViewFactory.createView("AddTree", this);
            scene = new Scene(view);
            myViews.put("AddTree", scene);
        }

        return scene;
    }

}
