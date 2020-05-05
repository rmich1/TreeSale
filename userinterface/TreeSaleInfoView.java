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
import model.*;

import javax.swing.*;
import java.text.SimpleDateFormat;

/** The view to add a scout into the system*/
//==============================================================
public class TreeSaleInfoView extends View
{

    // Model

    // GUI components
    private TextField barcodeTF;
    private TextField costTF;
    private ComboBox paymentType;
    private TextField custNameTF;
    private TextField custPhoneTF;
    private TextField custEmailTF;

    private Button submitButton;
    private Button cancelButton;
    private TreeSale treeSale;
    private TextField statusTF;
    private TextField notesTF;
    private String stat;
    private String notes;

    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public TreeSaleInfoView(IModel insertSale)
    {
        super(insertSale, "TreeSaleInfoView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);
        try {
            Tree tree = new Tree(myModel.getState("barcode").toString());
            stat = tree.getState("status").toString();
            notes = tree.getState("Notes").toString();
        }catch(InvalidPrimaryKeyException e){

        }
        populateFields();
        myModel.subscribe("SellTreeResponse", this);
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
        barcodeTF = new TextField();
        costTF = new TextField();
        paymentType = new ComboBox();
        paymentType.getItems().addAll("Cash", "Check");
        custNameTF = new TextField();
        custPhoneTF = new TextField();
        custEmailTF = new TextField();
        statusTF = new TextField();
        notesTF = new TextField();
        barcodeTF.setEditable(false);
        statusTF.setEditable(false);
        notesTF.setEditable(false);

        //Labels
        Text prompt =new Text("Enter Customer Info");
        Label barcode = new Label("Barcode: ");
        Label cost = new Label("Cost: ");
        Label paymentTypeLabel = new Label("Payment Type: ");
        Label custName = new Label("Customer Name: ");
        Label custPhone = new Label("Customer Phone: ");
        Label custEmail = new Label("Customer Email: ");
        Label status = new Label("Status:");
        Label notesLabel = new Label("Notes:");

        grid.add(prompt, 0, 0);
        grid.add(barcode, 0, 1);
        grid.add(barcodeTF, 1, 1);
        grid.add(status, 0, 2);
        grid.add(statusTF, 1, 2);
        grid.add(notesLabel, 0, 3);
        grid.add(notesTF, 1, 3);
        grid.add(cost, 0, 4);
        grid.add(costTF, 1, 4);
        grid.add(paymentTypeLabel, 0, 5);
        grid.add(paymentType, 1, 5);
        grid.add(custName, 0, 6);
        grid.add(custNameTF, 1, 6);
        grid.add(custPhone, 0, 7);
        grid.add(custPhoneTF, 1, 7);
        grid.add(custEmail, 0, 8);
        grid.add(custEmailTF, 1, 8);

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
       barcodeTF.setText(myModel.getState("barcode").toString());
        costTF.setText(myModel.getState("cost").toString());
        statusTF.setText(stat);
        notesTF.setText(notes);




    }

    // process events generated from our GUI components
    //-------------------------------------------------------------
    public void processAction(Event evt){
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Is the amount $" + costTF.getText() + " correct?" );

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
             makeTree();
             updateTree();


           myModel.stateChangeRequest("Return", null);
        } else {
            alert.close();
            displayErrorMessage("Confirm Cost");
        }

        }



    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void makeTree(){
        Properties sale = new Properties();
        sale.setProperty("transactionId", myModel.getState("transactionId").toString());
        sale.setProperty("transactionType", "Tree Sale");
        sale.setProperty("sessionId", myModel.getState("sessionId").toString());
        sale.setProperty("barcode", barcodeTF.getText());
        sale.setProperty("barcodePrefix", barcodeTF.getText().substring(0,3));
        sale.setProperty("cost", costTF.getText());
        sale.setProperty("paymentType", paymentType.getValue().toString());
        if(custNameTF.getText().length() !=0){
            sale.setProperty("custName", custNameTF.getText());
        }
        if(custPhoneTF.getText().length() != 0){
            sale.setProperty("custPhone", custPhoneTF.getText());
        }
        if(custEmailTF.getText().length() != 0){
            sale.setProperty("custEmail", custEmailTF.getText());
        }

        TreeSale soldTree = new TreeSale(sale);
        soldTree.save();

    }
    public void updateTree(){
        try {
            Tree tree = new Tree(barcodeTF.getText());
            String barcode = tree.getState("barcode").toString();
            String treeType = tree.getState("treeType").toString();
            String status = "Sold";
            String dateStatusUpdated = tree.getState("dateStatusUpdated").toString();
            String notes = tree.getState("Notes").toString();
            Properties updateTree = new Properties();
            updateTree.setProperty("barcode", barcode);
            updateTree.setProperty("treeType", treeType);
            updateTree.setProperty("status", status);
            updateTree.setProperty("dateStatusUpdated", dateStatusUpdated);
            updateTree.setProperty("Notes", notes);
            Tree tree1 = new Tree(updateTree);
            tree1.updateStateInDatabase();
        }catch (InvalidPrimaryKeyException e){
            e.printStackTrace();
        }



    }
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
