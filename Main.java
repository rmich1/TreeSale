

// specify the package

// system imports

import event.Event;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;

// project imports

import model.TLC;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;



/** The class containing the main program  for the ATM application */

//==============================================================
public class Main extends Application
{

    private TLC myTLC;		// the main behavior for the application


    /** Main frame of the application */

    private Stage mainStage;


    // start method for this class, the main application object
    //----------------------------------------------------------
    public void start(Stage primaryStage)
    {
        System.out.println("ScoutSystem 1.00");


        // Create the top-level container (main frame) and add contents to it.
        MainStageContainer.setStage(primaryStage, "Scout System Version 1.00");
        mainStage = MainStageContainer.getInstance();

        // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
        // 'X' IN THE WINDOW), and show it.
        //Event handler is being created in the parameters, events are the users actions
        mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
        });

        try
        {
            myTLC = new TLC();
        }
        catch(Exception exc)
        {
            System.err.println("TLC.TLC - could not create TLC!");
            new Event(Event.getLeafLevelClassName(this), "TLC.<init>", "Unable to create TLC object", Event.ERROR);
            exc.printStackTrace();
        }


        WindowPosition.placeCenter(mainStage);

        mainStage.show();
    }



    /**
     * The "main" entry point for the application. Carries out actions to
     * set up the application
     */

    //----------------------------------------------------------
    public static void main(String[] args)
    {

        launch(args);
    }

}

