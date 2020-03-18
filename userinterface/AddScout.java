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
import java.text.SimpleDateFormat;

/** The view to add a scout into the system*/
//==============================================================
public class AddScout extends View
{

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

    private ComboBox status;

    private Button submitButton;
    private Button cancelButton;



    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddScout(IModel insertScout)
    {
        super(insertScout, "AddScout");

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
        myModel.subscribe("SubmissionResponse", this);
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
        scoutIdTF = new TextField();
        scoutIdTF.setStyle("-fx-focus-color: transparent");
        scoutIdTF.setEditable(false);
        firstNameTF = new TextField();
        middleNameTF = new TextField();
        lastNameTF = new TextField();
        dateOfBirthTF = new TextField();
        phoneNumberTF = new TextField();
        emailTF = new TextField();
        troopIdTF = new TextField();
        scoutIdTF = new TextField();
        //Labels
        Text prompt =new Text("Scout Information");
        Label scoutIdLabel = new Label("Scout ID: ");
        Label firstNameLabel = new Label("First Name: ");
        Label middleNameLabel = new Label("Middle Name: ");
        Label lastNameLabel = new Label("Last Name: ");
        Label DOBLabel = new Label("Date Of Birth YYYY-MM-DD");
        Label phoneNumberLabel = new Label("Phone Number: ");
        Label emailLabel = new Label("E-Mail: ");
        Label statusLabel = new Label("Status: ");
        Label troopIdLabel = new Label("Troop ID: ");
        //status combo box
        status = new ComboBox();
        status.getItems().addAll("Active", "Inactive");
        status.setValue("Active");
        status.setPromptText("Active");

        grid.add(prompt, 0, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameTF, 1, 1);
        grid.add(middleNameLabel, 0, 2);
        grid.add(middleNameTF, 1, 2);
        grid.add(lastNameLabel, 0, 3);
        grid.add(lastNameTF, 1, 3);
        grid.add(DOBLabel, 0, 4);
        grid.add(dateOfBirthTF, 1, 4);
        grid.add(phoneNumberLabel, 0, 5);
        grid.add(phoneNumberTF, 1, 5);
        grid.add(emailLabel, 0, 6);
        grid.add(emailTF, 1, 6);
        grid.add(statusLabel, 0, 7);
        grid.add(status, 1, 7);
        grid.add(troopIdLabel, 0, 8);
        grid.add(troopIdTF, 1, 8);


       // status.setValue("Active");

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> processAction(e));

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(cancelButton);
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



    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt) {
        clearErrorMessage();
       if(firstNameTF.getText().length() == 0){
           displayErrorMessage("Enter First Name");
       }

       else if(lastNameTF.getText().length() == 0){
           displayErrorMessage("Enter Last Name");
       }
       else if(dateOfBirthTF.getText().length()==0){
           displayErrorMessage("Enter Date Of Birth");
       }
       else if(phoneNumberTF.getText().length()==0){
           displayErrorMessage("Enter Phone Number");
       }
       else if(emailTF.getText().length()== 0){
           displayErrorMessage("Enter E-mail");
       }
       else if(troopIdTF.getText().length()==0){
           displayErrorMessage("Enter Troop ID");
       }

        else{

            Properties scout = new Properties();
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

           LocalDateTime today = LocalDateTime.now();
           String formattedDate = today.format(formatter);
            scout.setProperty("firstName", firstNameTF.getText());
            scout.setProperty("middleName", middleNameTF.getText());
            scout.setProperty("lastName", lastNameTF.getText());
            scout.setProperty("dateOfBirth", dateOfBirthTF.getText());
            scout.setProperty("phoneNumber", phoneNumberTF.getText());
            scout.setProperty("email", emailTF.getText());
            scout.setProperty("status", status.getValue().toString());
            scout.setProperty("dateStatusUpdated", formattedDate);
            scout.setProperty("troopId", troopIdTF.getText());

            //SubmitNewScout goes to ScoutTransaction State Change Request
           myModel.stateChangeRequest("SubmitNewScout", scout);

        }
    }
    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void updateState (String key, Object value)
    {
        if (key.equals("SubmissionResponse")) {
            if (value instanceof Pair) {
                Pair<String, Pair<String, Boolean>> response = (Pair) value;
                if (response.getValue().getValue()) {
                    displayErrorMessage(response.getValue().getKey());
                } else {
                    displayMessage(response.getValue().getKey());
                }

                if (scoutIdTF.getText() == null || scoutIdTF.getText().trim().isEmpty()) {
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
