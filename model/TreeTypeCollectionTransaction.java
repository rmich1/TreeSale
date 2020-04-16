package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class TreeTypeCollectionTransaction extends Transaction {
    private TreeTypeCollection treeTypes = new TreeTypeCollection();
    private TreeType selectedTreeType;
    private String noTreeTypeError = "";
    private Boolean error = false;


    //Constructor
    protected TreeTypeCollectionTransaction()  {
    }
    //----------------------------------------------------------



    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("UpdateTreeType", "NoTreeTypeError");

        myRegistry.setDependencies(dependencies);
    }
    //----------------------------------------------------------
    public Object getState(String key) {
        if(key.equals("TreeTypeList")){
            return treeTypes;
        }
        if(key.equals("TreeTypeSelected")){
            return selectedTreeType;
        }
        if(key.equals("NoTreeTypeError")){
            return noTreeTypeError;

        }
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if(key.equals("UpdateTreeType")){
            treeTypes.findTreeBarcodePrefix((String) value);
            if((int) treeTypes.getState("Count") > 0){
                createAndShowTreeTypeCollectionView();
                noTreeTypeError = "";
            }
            else{
                noTreeTypeError = "No Tree Types";
            }
        }
        if(key.equals("TreeTypeSelected")){
            try{
                selectedTreeType = new TreeType((String) value);
            }catch (InvalidPrimaryKeyException ex){
                ex.printStackTrace();
            }

        }

        myRegistry.updateSubscribers(key, this);

    }

    protected Scene createView() {
        Scene scene = myViews.get("SearchTreeType");

        if (scene == null) {
            View view = ViewFactory.createView("SearchTreeType", this);
            scene = new Scene(view);
            myViews.put("SearchTreeType", scene);
        }

        return scene;
    }

    private void createAndShowTreeTypeCollectionView() {
        Scene scene = myViews.get("TreeTypeCollectionView");

        if (scene == null) {
            View view = ViewFactory.createView("TreeTypeCollectionView", this);
            scene = new Scene(view);
            myViews.put("TreeTypeCollectionView", scene);
        }

        swapToView(scene);
    }

}