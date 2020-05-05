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
    private TextField startTimeHourTF;
    private TextField startTimeMinuteTF;
    private ComboBox startTimeAMPM;
    private TextField endTimeHourTF;
    private TextField endTimeMinuteTF;
    private ComboBox endTimeAMPM;
    private TextField startingCashTF;
    String militaryStartTime;



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

        Label endTime = new Label("End Time: hh: mm AM/PM");
        Label startingCash = new Label("Starting Cash :");
        Label startSemi = new Label(":");
        Label endSemi = new Label(":");

        startTimeHourTF = new TextField();
        startTimeMinuteTF = new TextField();

        startTimeAMPM = new ComboBox();

        endTimeHourTF = new TextField();
        endTimeMinuteTF = new TextField();
        endTimeAMPM = new ComboBox();
        endTimeAMPM.getItems().addAll("AM", "PM");
        startingCashTF = new TextField();
        endTimeHourTF.setPromptText("hh");
        endTimeMinuteTF.setPromptText("mm");
        startTimeAMPM.getItems().addAll("AM", "PM");
        startTimeAMPM.setValue("AM");
        endTimeAMPM.setValue("AM");
        startTimeHourTF.setPromptText("hh");
        startTimeMinuteTF.setPromptText("mm");
        TextField errorStart = new TextField();
        TextField errorEnd = new TextField();
        startTimeHourTF.textProperty().addListener((observable) -> {
            clearErrorMessage();
            int hour = Integer.parseInt(startTimeHourTF.getText());
            if(hour > 12 || hour < 1){
               displayErrorMessage("Enter Valid Time for Hours");
            }
    });
        startTimeMinuteTF.textProperty().addListener((observable) -> {
            clearErrorMessage();
            int min = Integer.parseInt(startTimeMinuteTF.getText());
            if(min > 59 || min < 0){
                displayErrorMessage("Enter Valid Time for Minutes");
            }
        });



        //Labels
        Text openPrompt =new Text("Open Shift");
        grid.add(openPrompt, 0, 0);
        grid.add(startTime, 0, 1);
        grid.add(startTimeHourTF, 1, 1);
        grid.add(startSemi, 2, 1);
        grid.add(startTimeMinuteTF, 3, 1);
        grid.add(startTimeAMPM, 4, 1);
        grid.add(endTime, 0, 2);
        grid.add(endTimeHourTF, 1, 2);
        grid.add(endSemi, 2, 2);
        grid.add(endTimeMinuteTF, 3, 2);
        grid.add(endTimeAMPM, 4, 2);
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
        if(startTimeHourTF.getText().length() == 0){
            displayErrorMessage("Enter Start Time Hours");
        }
        else if(startTimeMinuteTF.getText().length()==0){
            displayErrorMessage("Enter End Time Hours");
        }

        else if(endTimeHourTF.getText().length() == 0){
            displayErrorMessage("Enter End Time Hour");
        }
        else if(endTimeMinuteTF.getText().length()==0){
            displayErrorMessage("Enter End Time Minutes");
        }
        else if(startingCashTF.getText().length()==0){
            displayErrorMessage("Enter Starting Cash");
        }
        else{
            String militaryEndTime;
            int hours = Integer.parseInt(startTimeHourTF.getText());
            int minutes = Integer.parseInt(startTimeMinuteTF.getText());
            if(startTimeAMPM.getValue().equals("PM")){
                if(hours != 12) {
                    hours = hours + 12;
                    System.out.println(hours);
                }
                else{
                    hours = 12;

                }
            }
            if(startTimeAMPM.getValue().equals("AM") && startTimeHourTF.getText().equals("12")){
                militaryStartTime = "00:" + minutes;
            }
            else{
                militaryStartTime = hours + ":" + minutes;
            }
            int endHours = Integer.parseInt(endTimeHourTF.getText());
            int endMins = Integer.parseInt(endTimeMinuteTF.getText());
            if(endTimeAMPM.getValue().equals("PM")){
                if(endHours != 12) {
                    endHours = endHours + 12;

                }
                else{
                    endHours = 12;

                }
            }
            if(endTimeAMPM.getValue().equals("AM") && endTimeHourTF.getText().equals("12")){
                militaryEndTime = "00:" + minutes;
            }
            else{
                militaryEndTime = hours + ":" + minutes;
            }



            Properties session = new Properties();
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Date dateobj = new Date();
            String today = df.format(dateobj);
            session.setProperty("startDate", today);
            System.out.println(militaryStartTime);
            System.out.println(militaryEndTime);
            session.setProperty("startTime", militaryStartTime);
            session.setProperty("endTime", militaryEndTime);
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