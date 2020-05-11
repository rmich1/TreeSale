// specify the package
package userinterface;

// system imports

import exception.InvalidPrimaryKeyException;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// project imports
import impresario.IModel;
import javafx.util.Pair;
import model.Scout;
import model.ScoutCollection;
import model.Session;
import model.Shift;

import javax.swing.*;
import java.text.SimpleDateFormat;

/** The view to add a scout into the system*/
//==============================================================
public class OpenShift extends View
{

    // Model

    // GUI components
    private TextField sessionIDTF;
    ScoutCollection activeScouts = new ScoutCollection();
    private ComboBox scoutBox;
    private TextField scoutIdTF;
    private TextField startDateTF;

    private TextField startHourTF;
    private TextField startMinTF;
    private ComboBox startAMPM;
    private TextField endMinTF;
    private ComboBox endAMPM;
    private TextField endHourTF;

    private TextField endTimeTF;
    private TextField companionNameTF;
    private TextField companionHoursTF;
    private String startingCash;
    private String militaryStartTime;
    private String militaryEndTime;

    private Button addScout;
    private Button submitButton;
    private Button cancelButton;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    DateFormat updatedFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date dateobj = new Date();
    String today = df.format(dateobj);


    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public OpenShift(IModel insertShift)
    {
        super(insertShift, "OpenShift");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);
        startingCash = "0";
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
        titleText.setFill(Color.GREEN);

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
        Vector<String> scouts = new Vector<String>();
        Vector<Scout> scoutList = new Vector<Scout>();
        scoutList = activeScouts.findAllActiveScouts();
        for(int i = 0; i < scoutList.size(); i++){
            scouts.add(scoutList.get(i).getState("firstName").toString().concat(" ").concat(scoutList.get(i).getState("lastName").toString()));

        }


       scoutBox = new ComboBox(FXCollections.observableArrayList(scouts));

        scoutIdTF = new TextField();
        startDateTF = new TextField();
        sessionIDTF = new TextField();
        startHourTF = new TextField();
        startMinTF = new TextField();
        startAMPM = new ComboBox();
        startAMPM.getItems().addAll("AM", "PM");
        endAMPM = new ComboBox();
        endHourTF = new TextField();
        endMinTF = new TextField();
        endAMPM.getItems().addAll("AM", "PM");
        endTimeTF = new TextField();
        companionNameTF = new TextField();
        sessionIDTF.setEditable(false);
        scoutIdTF.setEditable(false);
        companionHoursTF = new TextField();
        startHourTF.setPromptText("HH");
        startMinTF.setPromptText("MM");
        endHourTF.setPromptText("HH");
        endHourTF.setPromptText("MM");




        //Labels
        Text prompt =new Text("Open Shift");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD,15));
        prompt.setFill(Color.RED);
        Label sessionId = new Label("Session ID: ");
        Label scoutId = new Label("Scout ID: ");
        Label startDate = new Label("Start Date: ");
        Label startTime = new Label("Start Time: ");
        Label companionName = new Label("Companion Name: ");
        Label endTime = new Label("End Time: ");
        Label scout = new Label("Scout");
        Label semi = new Label(":");
        Label semiEnd = new Label(":");
        Label companionHours = new Label("Companion Hours");
        grid.add(prompt, 0, 0);
        grid.add(sessionId, 0, 1);
        grid.add(sessionIDTF, 1, 1);
        grid.add(scout, 0, 2);
        grid.add(scoutBox, 1, 2);
        grid.add(startDate, 0, 3);
        grid.add(startDateTF, 1, 3);
        grid.add(startTime, 0, 4);
        grid.add(startHourTF, 1, 4);
        grid.add(semi, 2, 4);
        grid.add(startMinTF, 3, 4);
        grid.add(startAMPM, 4, 4);

        grid.add(companionName, 0, 5);
        grid.add(companionNameTF, 1, 5);
        grid.add(companionHours, 0, 6);
        grid.add(companionHoursTF, 1, 6);
        grid.add(endTime, 0, 7);
        grid.add(endHourTF, 1, 7);
        grid.add(semiEnd, 2, 7);
        grid.add(endMinTF, 3, 7);
        grid.add(endAMPM, 4, 7);


        addScout = new Button("Add Another Scout");
        addScout.setOnAction(e -> processMoreScouts(e));
        submitButton = new Button("Submit Shift");
        submitButton.setOnAction(e -> processAction(e));
        startAMPM.setValue("AM");
        endAMPM.setValue("AM");

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(addScout);
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

        sessionIDTF.setText(myModel.getState("sessionId").toString());

        startDateTF.setText(today);
        scoutBox.setPromptText("Choose a scout");



    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt) {

        clearErrorMessage();
        if(scoutBox.getValue()==null){
            displayErrorMessage("Choose a scout");
        }
        else if(companionNameTF.getText().length() == 0){
            displayErrorMessage("Enter Companion Name");
        }
        else if(companionHoursTF.getText().length()==0){
            displayMessage("Enter Companion Hours");
        }

        else if(endHourTF.getText().length() == 0){
            displayErrorMessage("Enter End Time");
        }
        else{


            Properties shift = new Properties();
            try {
                Session ses = new Session(sessionIDTF.getText());
                startingCash = ses.getStartingCash();
                System.out.println(startingCash);
            }catch (InvalidPrimaryKeyException e){
                e.printStackTrace();
            }
            ScoutCollection sc = new ScoutCollection();
            String[] name = new String[2];
            String names = scoutBox.getValue().toString();
            name = names.split("\\s+");
            Vector<String> scouts = new Vector<String>();
            Vector<Scout> scoutList = new Vector<Scout>();
            scoutList = sc.findScoutswithFNameLNameLikeActive(name);
            for(int i = 0; i < scoutList.size(); i++){
                scouts.add(scoutList.get(i).getState("scoutId").toString());

            }
            getMilitaryTime();
            shift.setProperty("sessionId", sessionIDTF.getText());
            shift.setProperty("startingCash", startingCash);
            shift.setProperty("startTime", militaryStartTime);
            shift.setProperty("companionName", companionNameTF.getText());
            shift.setProperty("companionHours", companionHoursTF.getText());
            shift.setProperty("endTime", militaryEndTime);
            shift.setProperty("scoutId",scouts.get(0));
            Shift shiftOpen = new Shift(shift);
            shiftOpen.save();

            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Shift has successfully been opened!");

            ButtonType buttonTypeYes = new ButtonType("OK");
            ButtonType buttonTypeNo = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {

                alert.close();
                myModel.stateChangeRequest("Return", null);
            } else {
                alert.close();
               displayErrorMessage("Add Scout Information");
            }
            //SubmitNewScout goes to ScoutTransaction State Change Request


        }
    }
    public void processMoreScouts(Event evt) {
        clearErrorMessage();
        if(scoutBox.getValue()==null){
            displayErrorMessage("Choose a scout");
        }
        else if(companionNameTF.getText().length() == 0){
            displayErrorMessage("Enter Companion Name");
        }

        else if(endHourTF.getText().length() == 0){
            displayErrorMessage("Enter End Time");
        }
        else{
            getMilitaryTime();
            ScoutCollection sc = new ScoutCollection();
            String[] name = new String[2];
            String names = scoutBox.getValue().toString();
            name = names.split("\\s+");
            Vector<String> scouts = new Vector<String>();
            Vector<Scout> scoutList = new Vector<Scout>();
            scoutList = sc.findScoutswithFNameLNameLikeActive(name);
            for(int i = 0; i < scoutList.size(); i++){
                scouts.add(scoutList.get(i).getState("scoutId").toString());
            }
            Properties shift = new Properties();
            try {
                Session ses = new Session(sessionIDTF.getText());
                startingCash = ses.getStartingCash();
                System.out.println(startingCash);
            }catch (InvalidPrimaryKeyException e){
                e.printStackTrace();
            }

            shift.setProperty("sessionId", sessionIDTF.getText());
           shift.setProperty("startTime", militaryStartTime);
            shift.setProperty("companionName", companionNameTF.getText());
            shift.setProperty("companionHours", companionHoursTF.getText());
            shift.setProperty("startingCash",startingCash);
            shift.setProperty("endTime", militaryEndTime);
            shift.setProperty("scoutId", scouts.get(0));
            Shift newShift = new Shift(shift);
            newShift.save();
            scoutBox.setValue("Choose Scout");
            scoutBox.setPromptText("Choose Scout");
           // startTimeTF.clear();
            companionNameTF.clear();
            companionHoursTF.clear();
            endHourTF.clear();
            endMinTF.clear();
            endAMPM.setValue("AM");
            startHourTF.clear();
            startMinTF.clear();
            startAMPM.setValue("AM");

            displayMessage("Enter Additional Scout Info");




        }
    }
    public void getMilitaryTime(){
        int hours = Integer.parseInt(startHourTF.getText());
        int minutes = Integer.parseInt(startMinTF.getText());
        if(startAMPM.getValue().equals("PM")){
            if(hours != 12) {
                hours = hours + 12;
                System.out.println(hours);
            }
            else{
                hours = 12;

            }
        }
        if(startAMPM.getValue().equals("AM") && startHourTF.getText().equals("12")){
            militaryStartTime = "00:" + minutes;
        }
        else{
            militaryStartTime = hours + ":" + minutes;
        }
        int endHours = Integer.parseInt(endHourTF.getText());
        int endMins = Integer.parseInt(endMinTF.getText());
        if(endAMPM.getValue().equals("PM")){
            if(endHours != 12) {
                endHours = endHours + 12;

            }
            else{
                endHours = 12;

            }
        }
        if(endAMPM.getValue().equals("AM") && endHourTF.getText().equals("12")){
            militaryEndTime = "00:" + minutes;
        }
        else{
            militaryEndTime = endHours + ":" + minutes;
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
