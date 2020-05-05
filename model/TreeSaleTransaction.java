package model;

import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.util.Pair;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Properties;


public class TreeSaleTransaction extends Transaction {
    private TreeSale treeSale = new TreeSale((Properties) null);
    private String treeSaleResponse = "";
    private Boolean error = false;


    public TreeSaleTransaction(TreeSale ts){
        super();

        if (ts != null) {
            treeSale = ts;
        }
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SellNewTree", "SellResponse");

        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key) {

          switch (key) {
           case "transactionId":
            case "transactionType":
            case "sessionId":
            case "barcode":
            case "barcodePrefix":
            case "cost":
            case "paymentType":
           case "custName":
           case "custPhone":
           case "custEmail":
             return treeSale.getState(key);
            case "SellResponse":
               return new Pair<>((String) treeSale.getState("transctionId"),
                       new Pair<>(treeSaleResponse, error));
            default:
                return null;

        }
    }

    public void stateChangeRequest(String key, Object value) {

        if (key.equals("SellNewTree")) {
            treeSale = new TreeSale((Properties) value);
            //treeSale.save();
            error = false;
            treeSaleResponse = "Tree Sold Successfully!";
            createAndShowTreeSaleInfo();
        }

        myRegistry.updateSubscribers(key, this);

    }
    //Creating the addScout View
    protected Scene createView() {
        Scene scene = myViews.get("TreeSaleView");

        if (scene == null) {
            View view = ViewFactory.createView("TreeSaleView", this);
            scene = new Scene(view);
            myViews.put("TreeSaleView", scene);
        }

        return scene;
    }
    public void createAndShowTreeSaleInfo(){
        Scene scene = myViews.get("TreeSaleInfoView");

        if (scene == null) {
            View view = ViewFactory.createView("TreeSaleInfoView", this);
            scene = new Scene(view);
            myViews.put("TreeSaleInfoView", scene);
        }

        swapToView(scene);
    }

}
