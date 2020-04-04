// specify the package
package userinterface;

// system imports

import exception.InvalidPrimaryKeyException;
import javafx.collections.FXCollections;
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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

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

    private TextField startTimeTF;
    private TextField endTimeTF;
    private TextField companionNameTF;
    private TextField companionHoursTF;
    private String startingCash;

    private Button addScout;
    private Button submitButton;
    private Button cancelButton;
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
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
        startTimeTF = new TextField();
        endTimeTF = new TextField();
        companionNameTF = new TextField();
        sessionIDTF.setEditable(false);
        scoutIdTF.setEditable(false);
        companionHoursTF = new TextField();




        //Labels
        Text prompt =new Text("Open Shift");
        Label sessionId = new Label("Session ID: ");
        Label scoutId = new Label("Scout ID: ");
        Label startDate = new Label("Start Date: ");
        Label startTime = new Label("Start Time: ");
        Label companionName = new Label("Companion Name: ");
        Label endTime = new Label("End Time: ");
        Label scout = new Label("Scout");
        Label companionHours = new Label("Companion Hours");
        grid.add(prompt, 0, 0);
        grid.add(sessionId, 0, 1);
        grid.add(sessionIDTF, 1, 1);
        grid.add(scout, 0, 2);
        grid.add(scoutBox, 1, 2);
        grid.add(startDate, 0, 3);
        grid.add(startDateTF, 1, 3);
        grid.add(startTime, 0, 4);
        grid.add(startTimeTF, 1, 4);
        grid.add(companionName, 0, 5);
        grid.add(companionNameTF, 1, 5);
        grid.add(companionHours, 0, 6);
        grid.add(companionHoursTF, 1, 6);
        grid.add(endTime, 0, 7);
        grid.add(endTimeTF, 1, 7);


        addScout = new Button("Add Another Scout");
        addScout.setOnAction(e -> processMoreScouts(e));
        submitButton = new Button("Submit Shift");
        submitButton.setOnAction(e -> processAction(e));

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(cancelButton);
        btnContainer.getChildren().add(addScout);
        btnContainer.getChildren().add(submitButton);


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
            displayErrorMessage("Chose a scout");
        }
        else if(companionNameTF.getText().length() == 0){
            displayErrorMessage("Enter Companion Name");
        }
        else if(companionHoursTF.getText().length()==0){
            displayMessage("Enter Companion Hours");
        }

        else if(endTimeTF.getText().length() == 0){
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

            shift.setProperty("sessionId", sessionIDTF.getText());
            shift.setProperty("startingCash", startingCash);
            shift.setProperty("startTime", startTimeTF.getText());
            shift.setProperty("companionName", companionNameTF.getText());
            shift.setProperty("companionHours", companionHoursTF.getText());
            shift.setProperty("endTime", endTimeTF.getText());
            shift.setProperty("scoutId",scouts.get(0));
            System.out.println(scouts.get(0));
            shift.setProperty("status", "Active");
            Shift shiftOpen = new Shift(shift);
            shiftOpen.save();
            displayMessage("Shift has been added sucessfully!");
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

        else if(endTimeTF.getText().length() == 0){
            displayErrorMessage("Enter End Time");
        }
        else{
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
            shift.setProperty("startTime", startTimeTF.getText());
            shift.setProperty("companionName", companionNameTF.getText());
            shift.setProperty("companionHours", companionHoursTF.getText());
            shift.setProperty("startingCash",startingCash);
            shift.setProperty("endTime", endTimeTF.getText());
            shift.setProperty("status", "Active");
            shift.setProperty("scoutId", scouts.get(0));
            Shift newShift = new Shift(shift);
            newShift.save();
            scoutBox.setValue("Choose Scout");
            scoutBox.setPromptText("Choose Scout");
            startTimeTF.clear();
            companionNameTF.clear();
            companionHoursTF.clear();
            endTimeTF.clear();

            displayMessage("Enter Additional Scout Info");




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
