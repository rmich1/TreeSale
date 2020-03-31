package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;

import model.Scout;
import model.ScoutCollection;

/**
 * View Brings back the scout collection from the user search the user can double click on the desired
 * scout name which will bring up the EditScoutInfo screen
 */

//==============================================================================
public class ScoutCollectionView extends View
{
    protected TableView<ScoutTableModel> tableOfScouts = new TableView();
    protected Button cancelButton;
    protected Button submitButton;

    protected userinterface.MessageView statusLog;


    //--------------------------------------------------------------------------
    public ScoutCollectionView(IModel scCollection)
    {
        super(scCollection, "ScoutCollectionView");


        // create a container for showing the contents
        VBox container = new VBox(30);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }

    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ScoutCollection scouts = (ScoutCollection)myModel.getState("ScoutList");
        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();


        Vector<Scout> entryList = (Vector)scouts.getState("Scouts");
        Enumeration<Scout> entries = entryList.elements();

        while (entries.hasMoreElements())
        {
            Scout nextScout = entries.nextElement();
            Vector<String> view = nextScout.getEntryListView();

            //  Add this list entry to the list
            ScoutTableModel nextTableRowData = new ScoutTableModel(view);
            tableData.add(nextTableRowData);



        }

        SortedList<ScoutTableModel> sortedList = new SortedList<>(tableData,
                Comparator.comparing(ScoutTableModel::getStatus));




        tableOfScouts.setItems(sortedList);




    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Scout Search");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
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
        grid.setPadding(new Insets(20, 280, 20, 170));

        Text prompt = new Text("Select Scout");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);


        //This is how it should look
        TableColumn<ScoutTableModel, String> scoutIdColumn = new TableColumn("Scout ID") ;
        scoutIdColumn.setMinWidth(100);
        scoutIdColumn.setCellValueFactory(new PropertyValueFactory<>("scoutId"));


        TableColumn<ScoutTableModel, String> firstNameColumn = new TableColumn("First Name") ;
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("firstName"));


        TableColumn<ScoutTableModel, String> middleNameColumn = new TableColumn("Middle Name") ;
        middleNameColumn.setMinWidth(100);
        middleNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("middleName"));


        TableColumn<ScoutTableModel, String> lastNameColumn = new TableColumn("Last Name") ;
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("lastName"));


        TableColumn<ScoutTableModel, String> dobColumn = new TableColumn("Date Of Birth") ;
        dobColumn.setMinWidth(100);
        dobColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("dateOfBirth"));

        TableColumn<ScoutTableModel, String> phoneNumberColumn = new TableColumn("Phone Number") ;
        phoneNumberColumn.setMinWidth(100);
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("phoneNumber"));

        TableColumn<ScoutTableModel, String> emailColumn = new TableColumn("Email") ;
        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("email"));

        TableColumn<ScoutTableModel, String> statusColumn = new TableColumn("Status") ;
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("status"));


        TableColumn<ScoutTableModel, String> troopIdColumn = new TableColumn("Troop ID") ;
        troopIdColumn.setMinWidth(100);
        troopIdColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("troopId"));

        TableColumn<ScoutTableModel, String> dateStatusUpdatedColumn = new TableColumn("Date Status Updated");
        dateStatusUpdatedColumn.setMinWidth(100);
        dateStatusUpdatedColumn.setCellValueFactory(new PropertyValueFactory<ScoutTableModel, String>("dateStatusUpdated"));




        tableOfScouts.getColumns().addAll(firstNameColumn, middleNameColumn, lastNameColumn,
                dobColumn, phoneNumberColumn, emailColumn, statusColumn, troopIdColumn);

        tableOfScouts.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processAccountSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfScouts);

        submitButton = new Button("Submit");
       submitButton.setOnAction(e -> processAccountSelected());

        cancelButton = new Button("Back");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Return", null));

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        btnContainer.getChildren().add(cancelButton);
        btnContainer.getChildren().add(submitButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }



    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
        myModel.stateChangeRequest(key, value);
    }

    //--------------------------------------------------------------------------
    protected void processAccountSelected()
    {
        ScoutTableModel selectedItem = tableOfScouts.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            myModel.stateChangeRequest("ScoutSelected", selectedItem.getScoutId());

        }
    }

    //--------------------------------------------------------------------------
    protected userinterface.MessageView createStatusLog(String initialMessage)
    {
        statusLog = new userinterface.MessageView(initialMessage);

        return statusLog;
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

	//--------------------------------------------------------------------------



}
