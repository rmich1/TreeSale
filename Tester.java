/**
 * @author Becky Michael and Andy Duong
 * @version 2/6/2020
 * Tester for the Book, Patron, BookCollection and PatronCollection classes
 */


import exception.InvalidPrimaryKeyException;
import model.Scout;

import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

public class Tester {
    public static void main(String[] args) throws InvalidPrimaryKeyException {
        Scanner myObj = new Scanner(System.in);
        String cont = "Y";
        Properties prop = new Properties();
        prop.setProperty("scoutId", "1");
        prop.setProperty("firstName", "Becky");
        Scout scout1 = new Scout(prop);

       System.out.println(scout1.toString());
        //--------------------------------------------------------------------------------------------
        // Insert a patron into the database
        //----------------------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------------------

    }
}




