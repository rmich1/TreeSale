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
import model.*;


//==============================================================
public class EditTreeTypeInfo extends View {

    // Model

    // GUI components
    private TextField barcodePrefixTF;
    private TextArea typeDescTA;
    private TextField costTF;

    private TreeType treeTypeEdit = new TreeType();
    private TreeTypeCollection treeTypeColl = new TreeTypeCollection();

    private Button updateButton;
    private Button deleteButton;
    private Button cancelButton;




    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public EditTreeTypeInfo(IModel editTreeType) {
        super(editTreeType, "EditTreeTypeInfo");


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
        titleText.setFill(Color.BLACK);

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
        barcodePrefixTF = new TextField();
        typeDescTA = new TextArea();
        costTF = new TextField();
        //Labels
        Text prompt = new Text("Edit Tree Type Information");
        Label barcodePrefix = new Label("Barcode Prefix: ");
        Label typeDesc = new Label("Type Description");
        Label cost = new Label("Cost");

        grid.add(prompt, 0, 0);
        grid.add(barcodePrefix, 0, 1);
        grid.add(barcodePrefixTF, 1, 1);
        grid.add(typeDesc, 0, 2);
        grid.add(typeDescTA, 1, 2);
        grid.add(cost, 0, 3);
        grid.add(costTF, 1, 3);

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
        barcodePrefixTF.setText((String) myModel.getState("barcodePrefix"));
        typeDescTA.setText((String) myModel.getState("typeDesc"));
        costTF.setText((String) myModel.getState("cost"));
    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt) {
        clearErrorMessage();
        Properties treeType = new Properties();
        String oldBarcodePrefix = (String) myModel.getState("barcodePrefix");


        if ((barcodePrefixTF.getText().length()==0) && (typeDescTA.getText().length() == 0) &&
                (costTF.getText().length()==0)){
            displayErrorMessage("Enter Required Fields");
        }
        //If user is changing the barcode prefix, it will find the old barcode and delete it in the database
        //Then it adds the new tree type barcode prefix into the database
        if(!oldBarcodePrefix.equals(barcodePrefixTF.getText())) {

            treeType.setProperty("barcodePrefix", barcodePrefixTF.getText());
            treeType.setProperty("typeDesc", typeDescTA.getText());
            treeType.setProperty("cost", costTF.getText());
            try {
                TreeType treeTypeDelete = new TreeType((String) myModel.getState("barcodePrefix"));
                treeTypeDelete.deleteInDatabase();
            }
            catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
            displayMessage("Tree Updated Successfully!");
            myModel.stateChangeRequest("EditBarcodePrefix", treeType);


        }
        else{
            treeType.setProperty("barcodePrefix", barcodePrefixTF.getText());
            treeType.setProperty("typeDesc", typeDescTA.getText());
            treeType.setProperty("cost", costTF.getText());
            myModel.stateChangeRequest("EditTreeType", treeType);





        }
    }
    private void processDelete() {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete tree type?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                alert.close();
                deleteTreeType();
            } else {
                alert.close();
                myModel.stateChangeRequest("Return", null);
            }
        }





    public void deleteTreeType(){

        try {
            TreeType treeTypeDelete = new TreeType((String) myModel.getState("barcodePrefix"));
            treeTypeDelete.deleteInDatabase();
            myModel.stateChangeRequest("DeleteTreeType", null);
            displayMessage("Tree Type Successfully Deleted!");
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

                if (barcodePrefixTF.getText() == null || barcodePrefixTF.getText().trim().isEmpty()) {
                    barcodePrefixTF.setText(response.getKey());
                }
            }
        }
        if (key.equals("EditBarcodePrefix")) {
            if (value instanceof Pair) {
                Pair<String, Pair<String, Boolean>> response = (Pair) value;
                if (response.getValue().getValue()) {
                    displayErrorMessage(response.getValue().getKey());
                } else {
                    displayMessage(response.getValue().getKey());
                }

                if (barcodePrefixTF.getText() == null || barcodePrefixTF.getText().trim().isEmpty()) {
                    barcodePrefixTF.setText(response.getKey());
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

                if (barcodePrefixTF.getText() == null || barcodePrefixTF.getText().trim().isEmpty()) {
                    barcodePrefixTF.setText(response.getKey());
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
