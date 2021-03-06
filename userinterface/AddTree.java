
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
import model.*;



/** The view to add a tree into the system*/
//==============================================================






    /** The view to add a tree into the system*/
    //==============================================================
    public class AddTree extends View
    {

        // Model

        // GUI components
        private TextField barcodeTF;
        private TextField treeTypeTF;
        private TextField dateStatusUpdatedTF;
        private TextArea notesTA;


        private ComboBox status;


    private Button submitButton;
    private Button cancelButton;
    private TreeTypeCollection type;




        // For showing error message
        private userinterface.MessageView statusLog;


        // constructor for this class -- takes a model object
        //----------------------------------------------------------
        public AddTree(IModel insertTree)
        {
            super(insertTree, "AddTree");


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
            titleText.setFill(Color.DARKGREEN);

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
        barcodeTF.setEditable(true);
        barcodeTF.textProperty().addListener((observable) -> {
            TreeTypeCollection tt = new TreeTypeCollection();
                if(barcodeTF.getLength() > 1) {
                    String treeType = tt.getTreeType(barcodeTF.getText().substring(0, 2));
                    treeTypeTF.setText(treeType);
                }
        });
        dateStatusUpdatedTF = new TextField();
        dateStatusUpdatedTF.setEditable(true);

        notesTA = new TextArea();
        notesTA.setEditable(true);
        treeTypeTF = new TextField();
        treeTypeTF.setEditable(false);

        //Labels
        Text prompt =new Text("Tree Information");
        prompt.setFill(Color.RED);
        prompt.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        Label barcode = new Label("Barcode: ");
        Label statusLabel = new Label("Status: ");
       Label treeTypeLabel = new Label("Tree Type");
        Label notes = new Label("Notes: ");
        //status combo box
        status = new ComboBox();
        status.getItems().addAll("Available");
        status.setValue("Available");
        status.setPromptText("Available");
        grid.add(prompt, 0, 0);
        grid.add(barcode, 0, 1);
        grid.add(barcodeTF, 1, 1);
        grid.add(treeTypeLabel, 0, 2);
        grid.add(treeTypeTF, 1, 2);
        grid.add(statusLabel, 0, 3);
        grid.add(status, 1, 3);
        grid.add(notes, 0, 4);
        grid.add(notesTA, 1, 4);

        // status.setValue("Active");
        LocalDate date = LocalDate.now();
        dateStatusUpdatedTF.setText(date.toString());
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
        TreeCollection collection = new TreeCollection();
        clearErrorMessage();
        TreeTypeCollection tt = new TreeTypeCollection();
        System.out.println(barcodeTF.getText().length());

        if(barcodeTF.getText().length()==0){
            displayErrorMessage("Enter Barcode");
        }
        else if (barcodeTF.getText().length() < 6) {
            displayErrorMessage("Enter 6 digit barcode");
        }
        else if (collection.isDuplicate(barcodeTF.getText().toString())) {
            displayErrorMessage("Barcode already exists in system");

        }
        else if (tt.getTreeType(barcodeTF.getText().substring(0, 2)).equals("Unknown")) {
            displayErrorMessage("Invalid Barcode");
        }
        else {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime today = LocalDateTime.now();
            String formattedDate = today.format(formatter);
            Properties tree = new Properties();
            tree.setProperty("barcode", barcodeTF.getText());
            tree.setProperty("treeType", treeTypeTF.getText());
            tree.setProperty("status", status.getValue().toString());
            tree.setProperty("dateStatusUpdated", formattedDate);
            tree.setProperty("Notes", notesTA.getText());


            //SubmitNewScout goes to TreeTransaction State Change Request
            myModel.stateChangeRequest("SubmitNewTree", tree);

        }
    }



    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------

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
