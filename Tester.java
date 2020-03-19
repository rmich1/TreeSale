/**
 * @author Becky Michael and Andy Duong
 * @version 2/6/2020
 * Tester for the Book, Patron, BookCollection and PatronCollection classes
 */


import exception.InvalidPrimaryKeyException;
import model.Scout;
import model.Tree;

import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

public class Tester {
    public static void main(String[] args) throws InvalidPrimaryKeyException {
        Scanner myObj = new Scanner(System.in);
        String cont = "Y";

        Properties prop = new Properties();
        prop.setProperty("barcode", "FIR2999999");
        prop.setProperty("treeType", "Douglas");
        prop.setProperty("status", "Active");
        prop.setProperty("dateStatusUpdated",  "2020-03-19");
        prop.setProperty("Notes", "Good");
        Tree tree = new Tree(prop);
        tree.save();
        System.out.println("yay");



        //--------------------------------------------------------------------------------------------
        // Insert a patron into the database
        //----------------------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------------------

    }
}




