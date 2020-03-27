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

            default:
                return null;
        }
    }
}
