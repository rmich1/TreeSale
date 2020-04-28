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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;
import javafx.util.Pair;
import model.Scout;
import model.ScoutCollection;
import model.Tree;
import model.TreeCollection;

/** The View to edit the Scout info*/
//==============================================================
public class EditTreeInfo extends View {

    // Model

    // GUI components
    private TextField barcodeTF;
    private TextField treeTypeTF;
    private TextField dateStatusUpdatedTF;
    private TextArea notesTF;
    private Tree treeEdit = new Tree();
    private TreeCollection treeColl = new TreeCollection();

    private ComboBox status;

    private Button updateButton;
    private Button deleteButton;
    private Button cancelButton;
    private Stage primary = new Stage();



    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public EditTreeInfo(IModel editTree) {
        super(editTree, "EditTreeInfo");


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
        titleText.setFill(Color.GREEN);

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
        barcodeTF = new TextField();
        barcodeTF.setEditable(true);
        treeTypeTF = new TextField();
        dateStatusUpdatedTF = new TextField();
        dateStatusUpdatedTF.setEditable(false);
        notesTF = new TextArea();
        //Labels
        Text prompt = new Text("Edit Tree Information");
        prompt.setFont(Font.font("Arial", FontWeight.BOLD,15));
        prompt.setFill(Color.RED);
        Label barcode = new Label("Barcode: ");
        Label treeType = new Label("Tree Type: ");
        Label statusLabel = new Label("Status: ");
        Label dateStatusUpdatedLabel = new Label("Date Status Updated");
        Label notes = new Label("Notes: ");
        //status combo box
        status = new ComboBox();
        status.getItems().addAll("Available", "Damaged");

        grid.add(prompt, 0, 0);
       grid.add(barcode, 0, 1);
       grid.add(barcodeTF, 1, 1);
       grid.add(treeType, 0, 2);
       grid.add(treeTypeTF, 1, 2);
       grid.add(statusLabel, 0, 3);
       grid.add(status, 1, 3);
       grid.add(notes, 0, 4);
       grid.add(notesTF, 1, 4);






        updateButton = new Button("Update");
        updateButton.setOnAction(e -> processAction(e));

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> processDelete());

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
        barcodeTF.setText((String) myModel.getState("barcode"));
        treeTypeTF.setText((String) myModel.getState("treeType"));
        dateStatusUpdatedTF.setText((String) myModel.getState("dateStatusUpdated"));
        notesTF.setText((String) myModel.getState("Notes"));
        status.setPromptText((String) myModel.getState("status"));
        status.setValue((String) myModel.getState("status"));
    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt) {
        Properties tree = new Properties();
        String oldBarcode = (String) myModel.getState("barcode");

        clearErrorMessage();
        if ((barcodeTF.getText().length()==0) && (treeTypeTF.getText().length() == 0) &&
                (dateStatusUpdatedTF.getText().length()==0) && (notesTF.getText().length()==0)){
            displayErrorMessage("Enter Required Fields");
        }
        if(!oldBarcode.equals(barcodeTF.getText())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime today = LocalDateTime.now();
            String formattedDate = today.format(formatter);
            String oldStatus = (String) myModel.getState("status");
            //If the status is updated change the dateStatusUpdated Field
            if(!oldStatus.equals(status.getValue().toString())){
                tree.setProperty("dateStatusUpdated", formattedDate);
            }
            //If status is unchanged keep original dateStatusUpdatedField
            else{
                tree.setProperty("dateStatusUpdated", dateStatusUpdatedTF.getText());
            }
            tree.setProperty("barcode", barcodeTF.getText());
            tree.setProperty("treeType", treeTypeTF.getText());
            tree.setProperty("status", status.getValue().toString());
            tree.setProperty("Notes", notesTF.getText());
            try {
                Tree treeDelete = new Tree((String) myModel.getState("barcode"));
                treeDelete.deleteInDatabase();
            }
            catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
            displayMessage("Tree Updated Successfully!");
            myModel.stateChangeRequest("EditBarcode", tree);


        }
        else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime today = LocalDateTime.now();
            String formattedDate = today.format(formatter);
            String oldStatus = (String) myModel.getState("status");
            //If the status is updated change the dateStatusUpdated Field
            if(!oldStatus.equals(status.getValue().toString())){
                tree.setProperty("dateStatusUpdated", formattedDate);
            }
            //If status is unchanged keep original dateStatusUpdatedField
            else{
                tree.setProperty("dateStatusUpdated", dateStatusUpdatedTF.getText());
            }



            tree.setProperty("barcode", barcodeTF.getText());
            tree.setProperty("treeType", treeTypeTF.getText());
            tree.setProperty("status", status.getValue().toString());
            tree.setProperty("Notes", notesTF.getText());
            myModel.stateChangeRequest("EditTree", tree);





        }
    }
    private void processDelete() {
        System.out.println(status.getValue());
        if(status.getValue().equals("Sold")){
            displayErrorMessage("Can't Delete Sold Tree");
        }
        else {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete tree?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                alert.close();
                deleteTree();
            } else {
                alert.close();
                myModel.stateChangeRequest("Return", null);
            }
        }
    }




    public void deleteTree(){

        try {
            Tree treeDelete = new Tree((String) myModel.getState("barcode"));
            treeDelete.deleteInDatabase();
            myModel.stateChangeRequest("DeleteTree", null);
            displayMessage("Tree Successfully Deleted!");
        }
        catch (InvalidPrimaryKeyException ex) {
            ex.printStackTrace();
        }

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

                if (barcodeTF.getText() == null || barcodeTF.getText().trim().isEmpty()) {
                    barcodeTF.setText(response.getKey());
                }
            }
        }
        if (key.equals("EditBarcode")) {
            if (value instanceof Pair) {
                Pair<String, Pair<String, Boolean>> response = (Pair) value;
                if (response.getValue().getValue()) {
                    displayErrorMessage(response.getValue().getKey());
                } else {
                    displayMessage(response.getValue().getKey());
                }

                if (barcodeTF.getText() == null || barcodeTF.getText().trim().isEmpty()) {
                    barcodeTF.setText(response.getKey());
                }
            }

        }
        if (key.equals("DeleteResponse")) {
            if (value instanceof Pair) {
                Pair<String, Pair<String, Boolean>> response = (Pair) value;
                if (response.getValue().getValue()) {
                    displayErrorMessage(response.getValue().getKey());
                } else {
                    displayMessage(response.getValue().getKey());
                }

                if (barcodeTF.getText() == null || barcodeTF.getText().trim().isEmpty()) {
                    barcodeTF.setText(response.getKey());
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
