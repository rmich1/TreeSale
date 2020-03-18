// tabs=4
//************************************************************
//	COPYRIGHT 2009/2015 Sandeep Mitra, Michael Steves and T M Rao, The
//    College at Brockport, State University of New York. -
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//************************************************************
//
// specify the package
package model;

// system imports
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;
import javafx.stage.Stage;

// project imports
import database.Persistable;
import impresario.ModelRegistry;
import impresario.IModel;
import impresario.IView;
import impresario.ISlideShow;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.WindowPosition;


/** The superclass for all Fast Trax Model Entities that are also
 *  Persistable */
//==============================================================
public abstract class EntityBase extends Persistable
        implements IModel
{
    protected ModelRegistry myRegistry;	// registry for entities interested in our events
    private int referenceCount;		// the number of others using us
    protected boolean dirty;		// true if the data has changed
    protected Properties persistentState;	// the field names and values from the database
    private String myTableName;				// the name of our database table

    protected Hashtable<String, Scene> myViews;
    protected Stage myStage;

    protected Properties mySchema;

    // forward declarations
    public abstract Object getState(String key);
    public abstract void stateChangeRequest(String key, Object value);
    protected abstract void initializeSchema(String tableName);

    // constructor for this class
    //----------------------------------------------------------
    protected EntityBase(String tablename)
    {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // save our table name for later
        myTableName = tablename;

        // extract the schema from the database, calls methods in subclasses
        initializeSchema(myTableName);

        // create a place to hold our state from the database
        persistentState = new Properties();

        // create a registry for subscribers
        myRegistry = new ModelRegistry("EntityBase." + tablename);	// for now

        // initialize the reference count
        referenceCount = 0;
        // indicate the data in persistentState matches the database contents
        dirty = false;
    }

    /** Register objects to receive state updates. */
    //----------------------------------------------------------
    public void subscribe(String key, IView subscriber)
    {
        // DEBUG: System.out.println("EntityBase[" + myTableName + "].subscribe");
        // forward to our registry
        myRegistry.subscribe(key, subscriber);
    }

    /** Unregister previously registered objects. */
    //----------------------------------------------------------
    public void unSubscribe(String key, IView subscriber)
    {
        // DEBUG: System.out.println("EntityBase.unSubscribe");
        // forward to our registry
        myRegistry.unSubscribe(key, subscriber);
    }


    //-----------------------------------------------------------------------------------
    // package level permission, only ObjectFactory should modify
    void incrementReferenceCount()
    {
        referenceCount++;
    }

    //-----------------------------------------------------------------------------------
    // package level permission, only ObjectFactory should modify
    void decrementReferenceCount()
    {
        referenceCount--;
    }

    //-----------------------------------------------------------------------------------
    // package level permission, only ObjectFactory should modify
    int getReferenceCount()
    {
        return referenceCount;
    }

    //-----------------------------------------------------------------------------------
    // package level permission, only ObjectFactory and others in same package should invoke
    void releaseAggregates()
    {
    }

    //-----------------------------------------------------------------------------
    public void swapToView(Scene otherView)
    {

        if (otherView == null)
        {
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(otherView);
        myStage.sizeToScene();

        //Place in center
        WindowPosition.placeCenter(myStage);

    }

}

