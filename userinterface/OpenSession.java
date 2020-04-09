// specify the package
package userinterface;

// system imports

import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;
import javafx.util.Pair;
import model.Scout;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;

/** The view to add a scout into the system*/
//==============================================================
public class OpenSession extends View
{

    // Model

    // GUI components
    private TextField startTimeTF;
    private TextField endTimeTF;
    private TextField startingCashTF;



    private Button submitButton;
    private Button cancelButton;



    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public OpenSession(IModel insertSession)
    {
        super(insertSession, "OpenSession");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);

        populateFields();
        myModel.subscribe("OpenResponse", this);
    }


    // Create the label (Text) for the title
    //-------------------------------------------------------------
    private Node createTitle()
    {

        Text titleText = new Text("Boy Scout Troop 209");
        titleText.setWrappingWidth(300);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.BLACK);

        return titleText;
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(30);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));



        //TextFields
       Label startTime = new Label("Start Time:");
       Label endTime = new Label("End Time:");
       Label startingCash = new Label("Starting Cash :");
       startTimeTF = new TextField();
       endTimeTF = new TextField();
       startingCashTF = new TextField();

        //Labels
        Text openPrompt =new Text("Open Shift");
        grid.add(openPrompt, 0, 0);
       grid.add(startTime, 0, 1);
       grid.add(startTimeTF, 1, 1);
       grid.add(endTime, 0, 2);
       grid.add(endTimeTF, 1, 2);
       grid.add(startingCash, 0, 3);
       grid.add(startingCashTF, 1, 3);

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> processAction(e));

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);


        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private userinterface.MessageView createStatusLog(String initialMessage)
    {
        statusLog = new userinterface.MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {



    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt) {
        clearErrorMessage();
        if(startTimeTF.getText().length() == 0){
            displayErrorMessage("Enter Start Time");
        }

        else if(endTimeTF.getText().length() == 0){
            displayErrorMessage("Enter End Time");
        }
        else if(startingCashTF.getText().length()==0){
            displayErrorMessage("Enter Starting Cash");
        }
        else{
            System.out.println(startTimeTF.getText());
            System.out.println(endTimeTF.getText());
            System.out.println(startingCashTF.getText());


            Properties session = new Properties();
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Date dateobj = new Date();
            String today = df.format(dateobj);
            System.out.println(today);
            session.setProperty("startDate", today);
            session.setProperty("startTime", startTimeTF.getText());
            session.setProperty("endTime", endTimeTF.getText());
            session.setProperty("startingCash", startingCashTF.getText());
            session.setProperty("endingCash", "0");
            session.setProperty("totalCheckTransactionAmount", "0");
            session.setProperty("status", "Active");


            //SubmitNewScout goes to ScoutTransaction State Change Request
            myModel.stateChangeRequest("OpenNewSession", session);


        }
    }
    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void updateState (String key, Object value)
    {


    }


    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage (String message)
    {
        statusLog.displayErrorMessage(message);
    }
    public void displayMessage (String message)
    {
        statusLog.displayMessage(message);
    }
    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage ()
    {
        statusLog.clearErrorMessage();
    }

}
