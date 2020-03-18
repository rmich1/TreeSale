// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLInsertStatement.java,v $
//
//	Reason: Represents a SQL Statement that can be applied to
//			a database.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: pwri0503 $ */
/** @version	$Revision: 1.1.1.2 $ */


// specify the package
package database;

// system imports
import java.util.Enumeration;
import java.util.Properties;


// project imports


// Beginning of DatabaseManipulator class
//---------------------------------------------------------------------------------------------------------
public class SQLInsertStatement extends SQLStatement
{
    /**
     *
     * This handles only equality in the WHERE clause. This also
     * expects that for numeric types in the WHERE clause, a separate
     * Properties object containing the column name and numeric type
     * indicator will be provided. For text types, no entry in this
     * Properties object is necessary.
     */
    //------------------------------------------------------------
    public SQLInsertStatement(Properties schema, 		// the name of the table to insert into
    						  Properties insertValues)	// the values to insert
	{
    	super();	// implicit, doesn't do anything, but what the hell

		// Begin construction of the actual SQL statement
		theSQLStatement = "INSERT INTO " + schema.getProperty("TableName");

		// Construct the column name list and values part of the SQL statement
		String theColumnNamesList = "";
		String theValuesString = "";

		// Now, traverse the Properties object. In this case, this loop
		// must go at least one or we will get an error back from the db
		Enumeration theValuesColumns = insertValues.propertyNames();

		while (theValuesColumns.hasMoreElements() == true)
		{
		
			if ((theValuesString.equals("") == true) && (theColumnNamesList.equals("") == true))
			{
		  		theValuesString += " VALUES ( ";
				theColumnNamesList += " ( ";
			}
			else
			{
				theValuesString += " , ";
				theColumnNamesList += " , ";
			}

		
			String theColumnName = (String)theValuesColumns.nextElement();
			// System.out.println("The column name is " + theColumnName);
			String theColumnValue = insertEscapes(insertValues.getProperty(theColumnName));
			// System.out.println("The column value is " + theColumnValue);
			theColumnNamesList += theColumnName;
			//	System.out.println("The list is " + theColumnNamesList);

			//System.out.println("Checking insertType");
			String insertType = schema.getProperty(theColumnName);
			//		System.out.println("InsertType = " + insertType);
			//System.out.println("Schema is : " + schema);

			if (insertType.equals("numeric") == true)
			{
				theValuesString += theColumnValue;
				//	System.out.println("Value string updated: " + theValuesString);
			}
			else
			{
				theValuesString += "'" + theColumnValue + "'";
				// System.out.println("2 - Value string updated: " + theValuesString);
			}

		} // end while

		if ((theValuesString.equals("") == false) && (theColumnNamesList.equals("") == false))
		// this must be the case for an insert statement
		{
			theValuesString += " ) ";
			theColumnNamesList += " ) ";
		}

		theSQLStatement += theColumnNamesList;
		theSQLStatement += theValuesString;

		theSQLStatement += ";";
	
	}
}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLInsertStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.3  2003/10/08 17:17:25  tomb
//	Reformatted slightly, cleaned up type detection.
//
//	Revision 1.2  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//
//	Revision 1.1  2003/10/01 01:21:37  tomb
//	Initial checkin, reflects behavior extracted from EasyVideo DatabaseMutator and Accessor.
//
