// specify the package
package userinterface;



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
import model.Tree;
import model.TreeCollection;
import model.TreeTypeCollection;

import java.text.SimpleDateFormat;

/** The view to add a tree into the system*/
//==============================================================
public class AddTreeType extends View
{

    // Model

    // GUI components
    private TextField barcodePrefixTF;
    private TextArea typeDescTA;
    private TextField costTF;




    private Button submitButton;
    private Button cancelButton;



    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddTreeType(IModel insertTreeType)
    {
        super(insertTreeType, "AddTreeType");

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
        barcodePrefixTF = new TextField();
        typeDescTA = new TextArea();
        costTF = new TextField();
        //Labels
        Text prompt =new Text("Tree Type Information");
        Label barcodePrefix = new Label("Barcode Prefix: ");
        Label typeDesc = new Label("Type Description: ");
        Label cost = new Label("Cost: ");
        grid.add(prompt, 0, 0);
        grid.add(barcodePrefix, 0, 1);
        grid.add(barcodePrefixTF, 1, 1);
        grid.add(typeDesc, 0, 2);
        grid.add(typeDescTA, 1, 2);
        grid.add(cost, 0, 3);
        grid.add(costTF, 1, 3);

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
        TreeTypeCollection collection = new TreeTypeCollection();
        clearErrorMessage();
        if(barcodePrefixTF.getText().length() == 0){
            displayErrorMessage("Enter Barcode Prefix");
        }
        else if(collection.isDuplicate(barcodePrefixTF.getText().toString())){
            displayErrorMessage("Barcode Prefix already exists in system");
        }
        else if(costTF.getText().length()==0){
            displayErrorMessage("Enter Cost");
        }

        else {

            Properties treeType = new Properties();
            treeType.setProperty("barcodePrefix", barcodePrefixTF.getText());
            treeType.setProperty("typeDesc", typeDescTA.getText());
            treeType.setProperty("cost", costTF.getText());



            //SubmitNewScout goes to TreeTransaction State Change Request
            myModel.stateChangeRequest("SubmitNewTreeType", treeType);
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
