// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLDeleteStatement.java,v $
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
public class SQLDeleteStatement extends SQLStatement
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
    public SQLDeleteStatement(Properties schema, 		// the table schema
    						  Properties whereValues	// the values to delete
							 )
	{
    	super();	// implicit, doesn't do anything, but what the hell

		// Begin construction of the actual SQL statement
		theSQLStatement = "DELETE FROM " + schema.getProperty("TableName");

		// Construct the WHERE part of the SQL statement
		String theWhereString = "";

		// Now, traverse the WHERE clause Properties object
		if (whereValues != null)
		{
			Enumeration theWhereColumns = whereValues.propertyNames();
			while (theWhereColumns.hasMoreElements() == true)
			{
				if (theWhereString.equals(""))
				{
					theWhereString += " WHERE ";
				}
				else
				{
					theWhereString += " AND ";
				}

				String theColumnName = (String)theWhereColumns.nextElement();
				String theColumnValue = insertEscapes(whereValues.getProperty(theColumnName));

				if (theColumnValue.equals("NULL"))
				{
					theWhereString += theColumnName + " IS NULL";
				}
				else
				{
					String actualType = "Text";

					String whereTypeValue = schema.getProperty(theColumnName);
					if (whereTypeValue != null)
					{
						actualType = whereTypeValue;
					}

					actualType = actualType.toLowerCase();

					if (actualType.equals("numeric") == true)
					{
						theWhereString += theColumnName + " = " + theColumnValue;
					}
					else
					{
						theWhereString += theColumnName + " = '" + theColumnValue + "'";

					}
				}
			}
		}

		theSQLStatement += theWhereString;

		theSQLStatement += ";";
	}
}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLDeleteStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.2  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//
//	Revision 1.1  2003/10/01 01:21:37  tomb
//	Initial checkin, reflects behavior extracted from EasyVideo DatabaseMutator and Accessor.
//
