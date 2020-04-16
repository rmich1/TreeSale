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

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;

/** The view to add a scout into the system*/
//==============================================================
public class OpenSession extends View
{

    // Model

    // GUI components
    private ComboBox startHour;
    private ComboBox startMin;
    private ComboBox amPm;
    private ComboBox endHour;
    private ComboBox endMin;
    private ComboBox endAMPM;
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
       Label startTimeLabel = new Label("Start Time:");
       Label endTimeLabel = new Label("End Time:");
       Label startingCash = new Label("Starting Cash :");
       startHour = new ComboBox();
       startMin = new ComboBox();
        startHour.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12");
        startHour.setPromptText("Hour");
        startMin.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
                "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59");
        startMin.setPromptText("Minute");
        amPm = new ComboBox();
        amPm.getItems().addAll("AM", "PM");
        amPm.setPromptText("AM/PM");
        endHour = new ComboBox();
        endHour.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10", "11", "12");
        endMin = new ComboBox();
        endMin.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07",
                "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
                "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46",
                "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59");
        endAMPM = new ComboBox();
        endAMPM.getItems().addAll("AM", "PM");
        endMin.setPromptText("Minute");
        endHour.setPromptText("Hour");
        endAMPM.setPromptText("AM/PM");

       startingCashTF = new TextField();

        //Labels
        Text openPrompt =new Text("Open Shift");
        grid.add(openPrompt, 0, 0);
       grid.add(startTimeLabel, 0, 1);
       grid.add(startHour, 1, 1);
       grid.add(startMin, 2, 1);
       grid.add(amPm, 3, 1);
       grid.add(endTimeLabel, 0,2 );
       grid.add(endHour, 1, 2);
       grid.add(endMin, 2,2 );
       grid.add(endAMPM, 3, 2);
       grid.add(startingCash, 0, 6);
       grid.add(startingCashTF, 1, 6);

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
        if(startHour.getValue().equals(null)){
            displayErrorMessage("Enter Start Time");
        }

        else if(startMin.getValue().equals(null)){
            displayErrorMessage("Enter Start Time");
        }
        else if(amPm.getValue().equals(null)){
            displayErrorMessage("Enter AM/PM");
        }
        else if(endHour.getValue().equals(null)){
            displayErrorMessage("Enter End Time");
        }
        else if(endMin.getValue().equals(null)){
            displayErrorMessage("Enter End Time");
        }
        else if(endAMPM.getValue().equals(null)){
            displayErrorMessage("Enter AM/PM");
        }
        else if(startingCashTF.getText().length()==0){
            displayErrorMessage("Enter Starting Cash");
        }
        else{


            Properties session = new Properties();
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Date dateobj = new Date();
            String today = df.format(dateobj);
            String startTime=null;
            String endTime = null;
            int adjustedTime = 0;
            if(amPm.getValue().equals("AM")){
                startTime = (String)startHour.getValue() + ":" + (String)startMin.getValue() + ":00";
                System.out.println(startTime);
            }
            else{
                adjustedTime = (int)startHour.getValue();
                adjustedTime = adjustedTime + 12;
                Integer obj = new Integer(adjustedTime);
                startTime =  obj.toString() + ":" + (String)startMin.getValue() + ":00";
            }
            if(endAMPM.getValue().equals("AM")){
                endTime = (String)endHour.getValue() + ":" + (String)endMin.getValue() + ":00";
            }
            else{
                adjustedTime = (int)endHour.getValue();
                adjustedTime = adjustedTime + 12;
                Integer obj = new Integer(adjustedTime);
                endTime = obj.toString() + ":" + (String)endMin.getValue() + ":00";
            }
            session.setProperty("startDate", today);
            session.setProperty("startTime", startTime);
            session.setProperty("endDate", today);
            session.setProperty("endTime", endTime);
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
