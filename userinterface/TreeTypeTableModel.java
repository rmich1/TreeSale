package userinterface;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class TreeTypeTableModel {
    private final SimpleStringProperty barcodePrefix;
    private final SimpleStringProperty typeDesc;
    private final SimpleStringProperty cost;



    //----------------------------------------------------------------------------
    public TreeTypeTableModel(Vector<String> treeTypeData) {
        barcodePrefix = new SimpleStringProperty(treeTypeData.elementAt(0));
       typeDesc = new SimpleStringProperty(treeTypeData.elementAt(1));
       cost = new SimpleStringProperty(treeTypeData.elementAt(2));
    }

    //----------------------------------------------------------------------------

    public String getBarcodePrefix() {
        return barcodePrefix.get();
    }

    //----------------------------------------------------------------------------
    public void setBarcodePrefix(String code) {
        barcodePrefix.set(code);
    }
    //----------------------------------------------------------------------------

    public String getTypeDesc(){return typeDesc.get();}
    //----------------------------------------------------------------------------
    public void setTypeDesc(String desc){typeDesc.set(desc);}
    //----------------------------------------------------------------------------
    public String getCost(){return cost.get();}
    //----------------------------------------------------------------------------
    public void setCost(String cst){cost.set(cst);}
    //----------------------------------------------------------------------------
    public String getBarcodePrefixSort() {
        return barcodePrefix.get().toLowerCase();
    }

}
