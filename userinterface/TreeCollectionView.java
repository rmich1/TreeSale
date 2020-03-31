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
import model.Tree;
import model.TreeCollection;

/**
 * View Brings back the tree collection from the user search the user can double click on the desired
 * scout name which will bring up the EditTreeInfo screen
 */

//==============================================================================
public class TreeCollectionView extends View
{
    protected TableView<TreeTableModel> tableOfTrees = new TableView();
    protected Button cancelButton;
    protected Button submitButton;

    protected userinterface.MessageView statusLog;


    //--------------------------------------------------------------------------
    public TreeCollectionView(IModel tCollection)
    {
        super(tCollection, "TreeCollectionView");


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

        TreeCollection trees = (TreeCollection)myModel.getState("TreeList");
        ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();


        Vector<Tree> entryList = (Vector)trees.getState("Trees");
        Enumeration<Tree> entries = entryList.elements();

        while (entries.hasMoreElements())
        {
            Tree nextTree = entries.nextElement();
            Vector<String> view = nextTree.getEntryListView();

            //  Add this list entry to the list
            TreeTableModel nextTableRowData = new TreeTableModel(view);
            tableData.add(nextTableRowData);



        }

        SortedList<TreeTableModel> sortedList = new SortedList<TreeTableModel>(tableData,
                Comparator.comparing(TreeTableModel::getBarcodeSort));




        tableOfTrees.setItems(sortedList);




    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Tree Search");
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
        grid.setPadding(new Insets(20, 80, 20, 80));

        Text prompt = new Text("Select Tree");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);


        //This is how it should look
        TableColumn<TreeTableModel, String> barcodeColumn = new TableColumn("Barcode") ;
        barcodeColumn.setMinWidth(100);
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));


        TableColumn<TreeTableModel, String> treeTypeColumn = new TableColumn("Tree Type") ;
        treeTypeColumn.setMinWidth(100);
        treeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("treeType"));


        TableColumn<TreeTableModel, String> statusColumn = new TableColumn("Status") ;
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));


        TableColumn<TreeTableModel, String> dateStatusUpdatedColumn = new TableColumn("Date Of Status Updated") ;
        dateStatusUpdatedColumn.setMinWidth(100);
        dateStatusUpdatedColumn.setCellValueFactory(new PropertyValueFactory<>("dateStatusUpdated"));


        TableColumn<TreeTableModel, String> NotesColumn = new TableColumn("Notes") ;
        NotesColumn.setMinWidth(100);
        NotesColumn.setCellValueFactory(new PropertyValueFactory<>("Notes"));


        tableOfTrees.getColumns().addAll(barcodeColumn, treeTypeColumn, statusColumn, dateStatusUpdatedColumn, NotesColumn);

        tableOfTrees.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processTreeSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfTrees);

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> processTreeSelected());

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
    protected void processTreeSelected()
    {
        TreeTableModel selectedItem = tableOfTrees.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            myModel.stateChangeRequest("TreeSelected", selectedItem.getBarcode());

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
