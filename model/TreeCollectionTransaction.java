package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class TreeCollectionTransaction extends Transaction {
    private TreeCollection trees = new TreeCollection();
    private Tree selectedTree;
    private String noTreeError = "";
    private Boolean error = false;


    //Constructor
    protected TreeCollectionTransaction()  {
    }
    //----------------------------------------------------------



    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("UpdateTree", "NoTreeError");

        myRegistry.setDependencies(dependencies);
    }
    //----------------------------------------------------------
    public Object getState(String key) {
        if(key.equals("TreeList")){
            return trees;
        }
        if(key.equals("TreeSelected")){
            return selectedTree;
        }
        if(key.equals("NoTreeError")){
            return noTreeError;

        }
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if(key.equals("UpdateTree")){
            trees.findTreeBarcode((String) value);
            if((int) trees.getState("Count") > 0){
                createAndShowTreeCollectionView();
                noTreeError = "";
            }
            else{
                noTreeError = "No Trees";
            }
        }
        if(key.equals("TreeSelected")){
            try{
                selectedTree = new Tree((String) value);
            }catch (InvalidPrimaryKeyException ex){
                ex.printStackTrace();
            }

        }

        myRegistry.updateSubscribers(key, this);

    }

    protected Scene createView() {
        Scene scene = myViews.get("SearchTree");

        if (scene == null) {
            View view = ViewFactory.createView("SearchTree", this);
            scene = new Scene(view);
            myViews.put("SearchTree", scene);
        }

        return scene;
    }

    private void createAndShowTreeCollectionView() {
        Scene scene = myViews.get("TreeCollectionView");

        if (scene == null) {
            View view = ViewFactory.createView("TreeCollectionView", this);
            scene = new Scene(view);
            myViews.put("TreeCollectionView", scene);
        }

        swapToView(scene);
    }

}