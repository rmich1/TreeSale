
// specify the package
package userinterface;

// system imports

import impresario.IModel;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.SessionCollection;

// project imports


/**
 * The class containing the Teller View  for the ATM application
 */
//==============================================================
public class TLCView extends View {

    // GUI stuff
    //private TextField userid;
    //private PasswordField password;
    private Button insertScout;
    private Button updateScout;
    private Button deleteScout;
    private Button insertTree;
    private Button updateTree;
    private Button deleteTree;
    private Button insertTreeType;
    private Button updateTreeType;
    private Button deleteTreeType;
    private Button openSession;
    private Button submitButton;

    // For showing error message
    //Dispaly area on the bottom of the screen
    private userinterface.MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public TLCView(IModel tlc) {
        //Every view class has a model/controller class
        //model/controller is the Teller
        //The root node is the node in which every other element is under


        super(tlc, "TLCView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        //VBox is added to getChildren.add(container)
        //container is a VBox
        //VBox is a vertical Box
        container.setPadding(new Insets(15, 5, 5, 5));

        // create a Node (Text) for showing the title
        container.getChildren().add(createTitle());

        // create a Node (GridPane) for showing data entry fields
        //UserId and Password TF
        container.getChildren().add(createFormContents());

        // Error message area
        //Every GUI screen has a title
        container.getChildren().add(createStatusLog("                          "));
        //Add container to the group
        getChildren().add(container);


        //populateFields();

        // STEP 0: Be sure you tell your model what keys you are interested in
        myModel.subscribe("LoginError", this);
    }

    // Create the label (Text) for the title of the screen
    //-------------------------------------------------------------
    private Node createTitle() {

        Text titleText = new Text("Boy Scout Troop 209");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);

        //Title gets added to the container
        return titleText;
    }

    // Create the main form contents
    //-------------------------------------------------------------
    private GridPane createFormContents() {
        //Gridpane is a grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        //H V is horizontal and vertical gaps
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // data entry fields
        //Label userName = new Label("User ID:");



        //Buttons
        insertScout = new Button("Add New Scout");
        insertScout.setOnAction(e -> myModel.stateChangeRequest("NewScout", null));
        updateScout = new Button("Update Scout");
        updateScout.setOnAction(e -> myModel.stateChangeRequest("SearchScout", null));
        deleteScout = new Button("Delete Scout");
        deleteScout.setOnAction(e -> myModel.stateChangeRequest("SearchScout", null));
        insertTree = new Button("Add Tree");
        insertTree.setOnAction(e -> myModel.stateChangeRequest("NewTree", null));
        updateTree = new Button("Update Tree");
        updateTree.setOnAction(e -> myModel.stateChangeRequest("SearchTree", null));
        deleteTree = new Button("Delete Tree");
        deleteTree.setOnAction(e -> myModel.stateChangeRequest("SearchTree", null));
        insertTreeType = new Button("Add Tree Type");
        insertTreeType.setOnAction(e -> myModel.stateChangeRequest("NewTreeType", null));
        updateTreeType = new Button("Update Tree Type");
        updateTreeType.setOnAction(e -> myModel.stateChangeRequest("SearchTreeType", null));
        deleteTreeType = new Button("Delete Tree Type");
        deleteTreeType.setOnAction(e -> myModel.stateChangeRequest("SearchTreeType", null));
        openSession = new Button("Open Session");
        openSession.setOnAction(e -> processOpenSession(e));
        submitButton = new Button("Done");
        submitButton.setOnAction(e -> Platform.exit());

        grid.add(insertScout, 0, 0);
        grid.add(updateScout, 0, 1);
        grid.add(deleteScout, 0, 2);
        grid.add(insertTree, 0, 3);
      // grid.add(updateTree, 0, 4);
     //  grid.add(deleteTree, 0, 5);
     //  grid.add(insertTreeType, 0, 6);
     //  grid.add(updateTreeType, 0, 7);
      // grid.add(openSession, 0, 8);

        grid.add(submitButton, 0, 4);

        return grid;
    }


    // Create the status log field
    //-------------------------------------------------------------
    private userinterface.MessageView createStatusLog(String initialMessage) {

        statusLog = new userinterface.MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    // This method processes events generated from our GUI components.
    // Make the ActionListeners delegate to this method
    //-------------------------------------------------------------
    public void processOpenSession(Event evt) {
        // DEBUG: System.out.println("TellerView.actionPerformed()");

        clearErrorMessage();
        SessionCollection sessionCol = new SessionCollection();
      //  if(sessionCol.isOpenSessions()==true){
        //    displayErrorMessage("Session is open");
        //}
       //else {

            myModel.stateChangeRequest("NewSession", null);
        }
        //}







    /**
     * Process userid and pwd supplied when Submit button is hit.
     * Action is to pass this info on to the teller object
     */
    //----------------------------------------------------------


    //---------------------------------------------------------
    public void updateState(String key, Object value) {


    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message) {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage() {
        statusLog.clearErrorMessage();
    }

}

