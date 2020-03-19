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
        Tree tree = new Tree("FIR123456");
        System.out.println(tree.toString());
        Properties prop = new Properties();
        prop.setProperty("barcode", "DOU123456");
        prop.setProperty("status", "Active");
        prop.setProperty("Notes", "Good");
        Tree tree2 = new Tree(prop);
        tree2.save();



        //--------------------------------------------------------------------------------------------
        // Insert a patron into the database
        //----------------------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------------------

    }
}




