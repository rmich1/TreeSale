// specify the package
package model;

// system imports
import java.util.Vector;
import javax.swing.JFrame;

// project imports

/** The class containing the TransactionFactory for the ATM application */
//==============================================================
public class TransactionFactory {

    /**
     *
     */
    //----------------------------------------------------------
    public static Transaction createTransaction(String transType, Object resource) {
        switch (transType) {

            case "NewScout":

                return new ScoutTransaction((Scout) resource) {
                };
            case "SearchScout":

                return new ScoutCollectionTransaction();

            case "ScoutSelected":
                return new ScoutEditTransaction((Scout) resource);
            case "NewTree":
                return new TreeTransaction((Tree) resource) {

                };
            case "SearchTree":
                return new TreeCollectionTransaction();
            case "TreeSelected":
                return new TreeEditTransaction((Tree) resource);
            case "NewTreeType":
                return new TreeTypeTransaction((TreeType) resource){

                };
            case "SearchTreeType":
                return new TreeTypeCollectionTransaction();
            case "TreeTypeSelected":
                 return new TreeTypeEditTransaction((TreeType) resource);
            case "NewSession":
                return new SessionTransaction((Session) resource){

                };
            case "SubmitSession":
                return new ShiftTransaction((Shift) resource){

                };
            case "SellTree":
                    return new TreeSaleTransaction((TreeSale) resource){

                    };

            default:
                return null;
        }
    }
}
