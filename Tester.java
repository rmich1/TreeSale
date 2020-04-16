/**
 * @author Becky Michael and Andy Duong
 * @version 2/6/2020
 * Tester for the Book, Patron, BookCollection and PatronCollection classes
 */


import exception.InvalidPrimaryKeyException;
import model.Scout;
import model.Tree;
import model.TreeSale;

import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

public class Tester {
    public static void main(String[] args) throws InvalidPrimaryKeyException {
        Scanner myObj = new Scanner(System.in);
        String cont = "Y";

        Properties prop = new Properties();
        prop.setProperty("transactionType", "Tree Sale");
        prop.setProperty("barcode", "FIR12345789");
        prop.setProperty("sessionId", "Active");
        prop.setProperty("dateStatusUpdated",  "2020-03-19");
        prop.setProperty("Notes", "Good");
        TreeSale trans = new TreeSale(prop);
        trans.save();
        System.out.println("yay");



        //--------------------------------------------------------------------------------------------
        // Insert a patron into the database
        //----------------------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------------------

    }
}




