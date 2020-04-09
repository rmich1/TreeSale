// specify the package
package userinterface;

// system imports

import exception.InvalidPrimaryKeyException;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;
import javafx.util.Pair;
import model.Scout;
import model.ScoutCollection;

/** The View to edit the Scout info*/
//==============================================================
public class EditScoutInfo extends View {

    // Model

    // GUI components
    private TextField firstNameTF;
    private TextField middleNameTF;
    private TextField lastNameTF;
    private TextField dateOfBirthTF;
    private TextField phoneNumberTF;
    private TextField emailTF;
    private TextField statusTF;
    private TextField troopIdTF;
    private TextField scoutIdTF;
    private TextField dateStatusUpdatedTF;
    private Scout scoutEdit = new Scout();
    private ScoutCollection scoutColl = new ScoutCollection();

    private ComboBox status;

    private Button updateButton;
    private Button deleteButton;
    private Button cancelButton;
    private DatePicker datePicker;



    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public EditScoutInfo(IModel editScout) {
        super(editScout, "EditScoutInfo");


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
        myModel.subscribe("EditResponse", this);
    }


    // Create the label (Text) for the title
    //-------------------------------------------------------------
    private Node createTitle() {

        Text titleText = new Text("Boy Scout Troop 209");
        titleText.setWrappingWidth(300);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);

        return titleText;
    }

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent() {
        VBox vbox = new VBox(30);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        //TextFields
        scoutIdTF = new TextField();
        // scoutIdTF.setStyle("-fx-focus-color: transparent");
        // scoutIdTF.setEditable(false);
        firstNameTF = new TextField();
        middleNameTF = new TextField();
        lastNameTF = new TextField();
        dateOfBirthTF = new TextField();
        phoneNumberTF = new TextField();
        emailTF = new TextField();
        troopIdTF = new TextField();
        dateStatusUpdatedTF = new TextField();
        dateStatusUpdatedTF.setEditable(false);
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.parse((String)myModel.getState("dateOfBirth")));

        // datePicker.setValue((LocalDate) myModel.getState("dateOfBirth"));
        //Labels
        Text prompt = new Text("Scout Information");

        prompt.setFill(Color.RED);
        prompt.setFont(Font.font("Arial", FontWeight.BOLD,15));

        //Label scoutIdLabel = new Label("Scout ID: ");
        Label firstNameLabel = new Label("First Name: ");
        Label middleNameLabel = new Label("Middle Name: ");
        Label lastNameLabel = new Label("Last Name: ");
        Label DOBLabel = new Label("Date Of Birth: ");
        Label phoneNumberLabel = new Label("Phone Number: ");
        Label emailLabel = new Label("E-Mail: ");
        Label statusLabel = new Label("Status: ");
        Label troopIdLabel = new Label("Troop ID: ");
        Label dateStatusUpdatedLabel = new Label("Date Status Updated");
        //status combo box
        status = new ComboBox();
        status.getItems().addAll("Active", "Inactive");

        grid.add(prompt, 0, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameTF, 1, 1);
        grid.add(middleNameLabel, 0, 2);
        grid.add(middleNameTF, 1, 2);
        grid.add(lastNameLabel, 0, 3);
        grid.add(lastNameTF, 1, 3);
        grid.add(DOBLabel, 0, 4);
        grid.add(datePicker, 1, 4);
        grid.add(phoneNumberLabel, 0, 5);
        grid.add(phoneNumberTF, 1, 5);
        grid.add(emailLabel, 0, 6);
        grid.add(emailTF, 1, 6);
        grid.add(statusLabel, 0, 7);
        grid.add(status, 1, 7);
        grid.add(troopIdLabel, 0, 8);
        grid.add(troopIdTF, 1, 8);



        // status.setValue("Active");

        updateButton = new Button("Update");
        updateButton.setOnAction(e -> processAction(e));

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> processDelete(e));

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(updateButton);
        btnContainer.getChildren().add(deleteButton);
        btnContainer.getChildren().add(cancelButton);




        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    // Create the status log field
    //-------------------------------------------------------------
    private userinterface.MessageView createStatusLog(String initialMessage) {
        statusLog = new userinterface.MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields() {
        //Populates the fields into the edit Scout screen
        scoutIdTF.setText((String) myModel.getState("scoutId"));
        firstNameTF.setText((String) myModel.getState("firstName"));
        middleNameTF.setText((String) myModel.getState("middleName"));
        lastNameTF.setText((String) myModel.getState("lastName"));
        // datePicker.setValue((LocalDate) myModel.getState("dateOfBirth"));
        phoneNumberTF.setText((String) myModel.getState("phoneNumber"));
        emailTF.setText((String) myModel.getState("email"));
        status.setPromptText((String) myModel.getState("status"));
        status.setValue((String) myModel.getState("status"));
        dateStatusUpdatedTF.setText((String) myModel.getState("dateStatusUpdated"));
        troopIdTF.setText((String) myModel.getState("troopId"));


    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt) {
        Properties scout = new Properties();
        String oldStatus = (String) myModel.getState("status");

        clearErrorMessage();
        if ((firstNameTF.getText().length() == 0) && (lastNameTF.getText().length() == 0)
                && (datePicker.getValue().equals(LocalDate.now())) && (phoneNumberTF.getText().length() == 0)
                && (emailTF.getText().length() == 0) && (troopIdTF.getText().length() < 6)) {
            displayErrorMessage("Enter Required Fields");
        } else {
            if((phoneNumberTF.getText().substring(0,1).equals("(")) && (phoneNumberTF.getText().substring(4,5).equals(")")) && (phoneNumberTF.getText().length()==14)){
                phoneNumberTF.setText(phoneNumberTF.getText().toString().substring(1,4).concat("-").concat(phoneNumberTF.getText().toString().substring(5,13)));
            }
            else if(phoneNumberTF.getText().length()==10){
                phoneNumberTF.setText(phoneNumberTF.getText().substring(0,3).concat("-").concat(phoneNumberTF.getText().substring(3,6))
                        .concat("-").concat(phoneNumberTF.getText().substring(6,10)));
            }

            //If the status is updated change the dateStatusUpdated Field
            if(!oldStatus.equals(status.getValue().toString())){
                LocalDate today = LocalDate.now();
                String todayDate = today.toString();
                scout.setProperty("dateStatusUpdated", todayDate);
            }
            //If status is unchanged keep original dateStatusUpdatedField
            else{
                scout.setProperty("dateStatusUpdated", dateStatusUpdatedTF.getText());
            }

            scout.setProperty("scoutId", myModel.getState("scoutId").toString());
            scout.setProperty("firstName", firstNameTF.getText());
            scout.setProperty("middleName", middleNameTF.getText());
            scout.setProperty("lastName", lastNameTF.getText());
            scout.setProperty("dateOfBirth", datePicker.getValue().toString());
            scout.setProperty("phoneNumber", phoneNumberTF.getText());
            scout.setProperty("email", emailTF.getText());
            scout.setProperty("status", status.getValue().toString());
            scout.setProperty("troopId", troopIdTF.getText());
            displayMessage("Scout Successfully Updated");
            myModel.stateChangeRequest("EditScout", scout);






        }
    }
    public void processDelete(Event e){
        Properties scout = new Properties();
        status.setValue("Inactive");


        scout.setProperty("scoutId", myModel.getState("scoutId").toString());
        scout.setProperty("firstName", firstNameTF.getText());
        scout.setProperty("middleName", middleNameTF.getText());
        scout.setProperty("lastName", lastNameTF.getText());
        scout.setProperty("dateOfBirth", datePicker.getValue().toString());
        scout.setProperty("phoneNumber", phoneNumberTF.getText());
        scout.setProperty("email", emailTF.getText());
        scout.setProperty("status", status.getValue().toString());
        scout.setProperty("dateStatusUpdated", LocalDate.now().toString());
        scout.setProperty("troopId", troopIdTF.getText());
        displayMessage("Scout was deleted");
        myModel.stateChangeRequest("DeleteScout", scout);


    }

    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void updateState (String key, Object value)
    {
        if (key.equals("EditResponse")) {
            if (value instanceof Pair) {
                Pair<String, Pair<String, Boolean>> response = (Pair) value;
                if (response.getValue().getValue()) {
                    displayErrorMessage(response.getValue().getKey());
                } else {
                    displayMessage(response.getValue().getKey());
                }

                if ((String)myModel.getState("scoutId") == null){
                    scoutIdTF.setText(response.getKey());
                }
            }
        }

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