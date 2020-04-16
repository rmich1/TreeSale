package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTableModel {
    private final SimpleStringProperty barcode;
    private final SimpleStringProperty treeType;
    private final SimpleStringProperty status;
    private final SimpleStringProperty dateStatusUpdated;
    private final SimpleStringProperty notes;


    //----------------------------------------------------------------------------
    public TreeTableModel(Vector<String> treeData) {
        barcode = new SimpleStringProperty(treeData.elementAt(0));
        treeType = new SimpleStringProperty(treeData.elementAt(1));
        status = new SimpleStringProperty(treeData.elementAt(2));
        dateStatusUpdated = new SimpleStringProperty(treeData.elementAt(3));
        notes = new SimpleStringProperty(treeData.elementAt(4));
    }

    //----------------------------------------------------------------------------

    public String getBarcode() {
        return barcode.get();
    }

    //----------------------------------------------------------------------------
    public void setBarcode(String code) {
        barcode.set(code);
    }
    //----------------------------------------------------------------------------


    public String getTreeType() {
        return treeType.get();
    }
    //----------------------------------------------------------------------------
    public void setTreeType(String type){
        treeType.set(type);
    }
    //----------------------------------------------------------------------------

    public String getStatus() {
        return status.get();
    }
    //----------------------------------------------------------------------------
    public void setStatus(String stat){
        status.set(stat);
    }
    //----------------------------------------------------------------------------

    public String getDateStatusUpdated() {
        return dateStatusUpdated.get();
    }
    public String getBarcodeSort() {
        return barcode.get().toLowerCase();
    }
    //----------------------------------------------------------------------------
    public void setDateStatusUpdated(String date){
        dateStatusUpdated.set(date);
    }
    //----------------------------------------------------------------------------

    public String getNotes() {
        return notes.get();
    }
    //----------------------------------------------------------------------------
    public void setNotes(String note){
        notes.set(note);
    }
    //----------------------------------------------------------------------------

}
