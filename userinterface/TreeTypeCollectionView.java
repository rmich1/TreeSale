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


import model.TreeType;
import model.TreeTypeCollection;

/**
 * View Brings back the tree collection from the user search the user can double click on the desired
 * scout name which will bring up the EditTreeInfo screen
 */

//==============================================================================
public class TreeTypeCollectionView extends View
{
    protected TableView<TreeTypeTableModel> tableOfTreeTypes = new TableView();
    protected Button cancelButton;
    protected Button submitButton;

    protected userinterface.MessageView statusLog;


    //--------------------------------------------------------------------------
    public TreeTypeCollectionView(IModel ttCollection)
    {
        super(ttCollection, "TreeTypeCollectionView");


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

        TreeTypeCollection treeTypes = (TreeTypeCollection) myModel.getState("TreeTypeList");
        ObservableList<TreeTypeTableModel> tableData = FXCollections.observableArrayList();


        Vector<TreeType> entryList = (Vector)treeTypes.getState("TreeTypes");
        Enumeration<TreeType> entries = entryList.elements();

        while (entries.hasMoreElements())
        {
            TreeType nextTreeType = entries.nextElement();
            Vector<String> view = nextTreeType.getEntryListView();

            //  Add this list entry to the list
            TreeTypeTableModel nextTableRowData = new TreeTypeTableModel(view);
            tableData.add(nextTableRowData);



        }

        SortedList<TreeTypeTableModel> sortedList = new SortedList<TreeTypeTableModel>(tableData,
                Comparator.comparing(TreeTypeTableModel::getBarcodePrefixSort));




        tableOfTreeTypes.setItems(sortedList);




    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Tree Type Search");
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
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 80, 20, 80));

        Text prompt = new Text("Select Tree Type");
        prompt.setWrappingWidth(100);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);


        //This is how it should look
        TableColumn<TreeTypeTableModel, String> barcodePrefixColumn = new TableColumn("Barcode Prefix") ;
        barcodePrefixColumn.setMinWidth(100);
        barcodePrefixColumn.setCellValueFactory(new PropertyValueFactory<>("barcodePrefix"));

        TableColumn<TreeTypeTableModel, String> typeDescColumn = new TableColumn("Type Description") ;
        typeDescColumn.setMinWidth(100);
       typeDescColumn.setCellValueFactory(new PropertyValueFactory<>("typeDesc"));

        TableColumn<TreeTypeTableModel, String> costColumn = new TableColumn("Cost") ;
        costColumn.setMinWidth(100);
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));



        tableOfTreeTypes.getColumns().addAll(barcodePrefixColumn, typeDescColumn, costColumn);

        tableOfTreeTypes.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processTreeTypeSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(75, 100);
        scrollPane.setContent(tableOfTreeTypes);

        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> processTreeTypeSelected());

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
    protected void processTreeTypeSelected()
    {
        TreeTypeTableModel selectedItem = tableOfTreeTypes.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            myModel.stateChangeRequest("TreeTypeSelected", selectedItem.getBarcodePrefix());

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
