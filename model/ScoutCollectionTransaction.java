package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ScoutCollectionTransaction extends Transaction {
    private ScoutCollection scouts = new ScoutCollection();
    private Scout selectedScout;
    private String noScoutsError = "";
    private Boolean error = false;


   //Constructor
    protected ScoutCollectionTransaction()  {
    }
    //----------------------------------------------------------



    protected void setDependencies() {
        dependencies = new Properties();
      //  dependencies.setProperty("SubmitScout", "NoScoutsError");
        dependencies.setProperty("UpdateFirstNameAct", "NoScoutsError");
        dependencies.setProperty("UpdateLastNameAct", "NoScoutsError");
        dependencies.setProperty("UpdateEmailAct", "NoScoutsError");
        dependencies.setProperty("UpdateFNameLNameAct", "NoScoutsError");
        dependencies.setProperty("UpdateFNameEmailAct", "NoScoutsError");
        dependencies.setProperty("UpdateLNameEmailAct", "NoScoutsError");
        dependencies.setProperty("UpdateAllAct", "NoScoutsError");
        dependencies.setProperty("UpdateFirstNameAll", "NoScoutsError");
        dependencies.setProperty("UpdateLastNameAll", "NoScoutsError");
        dependencies.setProperty("UpdateEmailAll", "NoScoutsError");
        dependencies.setProperty("UpdateFNameLNameAll", "NoScoutsError");
        dependencies.setProperty("UpdateFNameEmailAll", "NoScoutsError");
        dependencies.setProperty("UpdateLNameEmailAll", "NoScoutsError");
        dependencies.setProperty("UpdateAllActIn", "NoScoutsError");

        myRegistry.setDependencies(dependencies);
    }
    //----------------------------------------------------------
    public Object getState(String key) {
        if(key.equals("ScoutList")){
            return scouts;
        }
        if(key.equals("ScoutSelected")){
            return selectedScout;
        }
        if(key.equals("NoScoutsError")){
            return noScoutsError;

        }
        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if(key.equals("UpdateFirstNameAct")){
            scouts.findScoutsWithFNameLikeActive((String) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateLastNameAct")){
            scouts.findScoutsWithLNameLikeActive((String) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateEmailAct")){
            scouts.findScoutWithEmailLikeActive((String) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateFNameLNameAct")){
            scouts.findScoutswithFNameLNameLikeActive((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError="";
            }
            else {
                noScoutsError = "No Scouts";

            }
        }
        if(key.equals("UpdateAllAct")){
            scouts.findScoutwithAllinfoLikeActive((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else {
                noScoutsError = "No Scouts";
            }
        }
        if (key.equals("UpdateFNameEmailAct")) {

            scouts.findScoutswithFNameEmailLikeActive((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateLNameEmailAct")){
            scouts.findScoutswithLNameEmailLikeActive((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }

        }
        //-------------------------------------------------------------
        if(key.equals("UpdateFirstNameAll")){
            scouts.findScoutsWithFNameLike((String) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateLastNameAll")){
            scouts.findScoutsWithLNameLike((String) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateEmailAll")){
            scouts.findScoutWithEmailLike((String) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateFNameLNameAll")){
            scouts.findScoutswithFNameLNameLike((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError="";
            }
            else {
                noScoutsError = "No Scouts";

            }
        }
        if(key.equals("UpdateFNameEmailAll")){
            scouts.findScoutswithFNameEmailLike((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateLNameEmailAll")){
            scouts.findScoutswithLNameEmailLike((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        if(key.equals("UpdateAllActIn")){
            scouts.findScoutwithAllinfoLike((String[]) value);
            if((int) scouts.getState("Count") > 0){
                createAndShowScoutCollectionView();
                noScoutsError = "";
            }
            else{
                noScoutsError = "No Scouts";
            }
        }
        //---------------------------------------------------------------
        if(key.equals("ScoutSelected")){
            try{
                selectedScout = new Scout((String) value);
            }catch (InvalidPrimaryKeyException ex){
                ex.printStackTrace();
            }

        }
        myRegistry.updateSubscribers(key, this);

    }

    protected Scene createView() {
        Scene scene = myViews.get("SearchScout");

        if (scene == null) {
            View view = ViewFactory.createView("SearchScout", this);
            scene = new Scene(view);
            myViews.put("SearchScout", scene);
        }

        return scene;
    }

    private void createAndShowScoutCollectionView() {
        Scene scene = myViews.get("ScoutCollectionView");

        if (scene == null) {
            View view = ViewFactory.createView("ScoutCollectionView", this);
            scene = new Scene(view);
            myViews.put("ScoutCollectionView", scene);
        }

        swapToView(scene);
    }

}