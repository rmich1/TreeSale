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
public class CloseShiftView extends View
{

    // Model

    // GUI components
    private TextField sessionIDTF;
    private TextField startDateTF;
    private TextField startTimeTF;
    private TextField endTimeTF;
    private TextField endDateTF;
    private TextField startingCashTF;
    private TextField endingCashTF;
    private TextField totalCheckTransactionAmountTF;
    private Button submitButton;
    private Button cancelButton;
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    Date dateobj = new Date();
    String today = df.format(dateobj);
    String startDt;
    String startTm;
    String endDt;
    String endTm;
    String startingCsh;
    String totCash;
    String totCheck;
    String sessionId;


    // For showing error message
    private userinterface.MessageView statusLog;


    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public CloseShiftView(IModel insertShift)
    {
        super(insertShift, "CloseShiftView");
        ShiftCollection sc = new ShiftCollection();
        Vector<Shift> openShift = new Vector<Shift>();
        SessionCollection sescol = new SessionCollection();
        Vector<Session>openSession = new Vector<Session>();
      System.out.println(openSession = sescol.findOpenSessions());

        sessionId = openSession.get(0).getState("sessionId").toString();
        startDt = openSession.get(0).getState("startDate").toString();
        startTm = openSession.get(0).getState("startTime").toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        endDt = now.toString().substring(0,10);
        endTm = openSession.get(0).getState("endTime").toString();
        startingCsh = openSession.get(0).getStartingCash();
        Double startC = Double.parseDouble(startingCsh);
        TreeSaleCollection tsc = new TreeSaleCollection();
        Vector<TreeSale> treeSaleCost = new Vector<TreeSale>();
        treeSaleCost = tsc.findTotalCash(sessionId);
        double totalCash = 0;
        double totalCheck = 0;
        for(int i = 0; i < treeSaleCost.size(); i++){
            if(treeSaleCost.get(i).getState("paymentType").equals("Cash")){
                double cash = Double.parseDouble(treeSaleCost.get(i).getState("cost").toString());
                totalCash = totalCash + cash;
            }
        }
        for(int i = 0; i < treeSaleCost.size(); i++){
            if(treeSaleCost.get(i).getState("paymentType").equals("Check")){
                double cash = Double.parseDouble(treeSaleCost.get(i).getState("cost").toString());
                totalCheck = totalCheck + cash;
            }
        }
        startC = startC + totalCash;
        totCash = "" + startC + "";
        totCheck = "" + totalCheck + "";

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
        myModel.subscribe("CloseResponse", this);



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

        startDateTF = new TextField();
        sessionIDTF = new TextField();
        startTimeTF = new TextField();
        endDateTF = new TextField();
        endTimeTF = new TextField();
        sessionIDTF.setEditable(false);
        startingCashTF = new TextField();
        endingCashTF = new TextField();
        totalCheckTransactionAmountTF = new TextField();

        //Labels
        Text prompt =new Text("Close Shift");
        Label sessionId = new Label("Session ID: ");
        Label startDate = new Label("Start Date: ");
        Label startTime = new Label("Start Time: ");
        Label endTime = new Label("End Time: ");
        Label endDate = new Label("End Date: ");
        Label startingCash = new Label("Starting Cash: ");
        Label endingCash = new Label("Ending Cash: ");
        Label totalCheckTrans = new Label("Total Check Transaction Amount: ");
        grid.add(prompt, 0, 0);
        grid.add(sessionId, 0, 1);
        grid.add(sessionIDTF, 1, 1);
        grid.add(startDate, 0, 2);
        grid.add(startDateTF, 1, 2);
        grid.add(startTime, 0, 3);
        grid.add(startTimeTF, 1, 3);
        grid.add(endDate, 0, 4);
        grid.add(endDateTF, 1, 4);
        grid.add(endTime, 0, 5);
        grid.add(endTimeTF, 1, 5);
        grid.add(startingCash, 0, 6);
        grid.add(startingCashTF, 1, 6);
        grid.add(endingCash, 0, 7);
        grid.add(endingCashTF, 1, 7);
        grid.add(totalCheckTrans, 0, 8);
        grid.add(totalCheckTransactionAmountTF, 1, 8);


        submitButton = new Button("Close Shift");
        submitButton.setOnAction(e -> processClose());

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
        sessionIDTF.setText(sessionId);
        startDateTF.setText(startDt);
        startTimeTF.setText(startTm);
        endDateTF.setText(endDt);
        endTimeTF.setText(endTm);
        startingCashTF.setText(startingCsh);
        endingCashTF.setText(totCash);
        totalCheckTransactionAmountTF.setText(totCheck);





    }

    // process events generated from our GUI components
    //-------------------------------------------------------------


    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void processClose(){
        try{
            Session closeSession = new Session(sessionId);
            Properties closeSes = new Properties();
            closeSes.setProperty("sessionId", sessionId);
            closeSes.setProperty("startDate", startDateTF.getText());
            closeSes.setProperty("startTime", startTimeTF.getText());
            closeSes.setProperty("endDate", endDateTF.getText());
            closeSes.setProperty("endTime", endTimeTF.getText());
            closeSes.setProperty("startingCash", startingCashTF.getText());
            closeSes.setProperty("totalCheckTransactionAmount", totalCheckTransactionAmountTF.getText());
            closeSes.setProperty("endingCash", endingCashTF.getText());
            closeSes.setProperty("status", "Inactive");
            Session close = new Session(closeSes);
            close.save();
        }catch(InvalidPrimaryKeyException e){
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
