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
import model.*;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;

/** The view to add a scout into the system*/
//==============================================================
public class TreeSaleView extends View
{

    // Model

    // GUI components
    private TextField barcodeTf;
   private Tree treeSale;
   private TreeType treeType;


    private Button submitButton;
    private Button cancelButton;



    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public TreeSaleView(IModel sellTree)
    {
        super(sellTree, "TreeSaleView");

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
        Label barcode = new Label("Barcode: ");
        barcodeTf = new TextField();

        //Labels
        Text sellPrompt =new Text("Provide Tree Barcode");
        grid.add(sellPrompt, 0, 0);
        grid.add(barcode, 0, 1);
        grid.add(barcodeTf, 1, 1);


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
        if(barcodeTf.getText().length() == 0){
            displayErrorMessage("Enter Barcode");
        }
        else {
            try {
                 treeSale = new Tree(barcodeTf.getText());
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
            if (treeSale.getState("status").equals("Sold")) {
                displayErrorMessage("Can't Sell a Sold Tree!");
            } else {

                Properties transaction = new Properties();
                String barcodePrefix = barcodeTf.getText().substring(0,3);
                try {
                    treeType = new TreeType(barcodePrefix);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }

                SessionCollection sessionCollection = new SessionCollection();
                Vector<Session> sessionsOpen = new Vector<Session>();
                sessionsOpen = sessionCollection.findOpenSessions();

                String sessionID = sessionsOpen.get(0).getState("sessionId").toString();
                System.out.println(sessionID);
                transaction.setProperty("sessionId", sessionID);
                transaction.setProperty("transactionType", "Tree Sale");
                transaction.setProperty("barcodePrefix", barcodePrefix);
                transaction.setProperty("barcode", barcodeTf.getText());
                transaction.setProperty("cost", treeType.getState("cost").toString());


                //SubmitNewScout goes to ScoutTransaction State Change Request
                myModel.stateChangeRequest("SellNewTree", transaction);


            }
        }
    }
    /**
     * Required by interface, but has no role here
     */
    //
    public void getTreeInfo(){

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
