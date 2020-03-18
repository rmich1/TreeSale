// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLQueryStatement.java,v $
//
//	Reason: Represents a SQL Statement used to query for 
//			multiple records in the database.
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
public class SQLQueryStatement extends SQLStatement
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
    public SQLQueryStatement(Properties selSchema,
    						 Properties projectionSchema,
    						 Properties selectionValues)
    {
    	
    	/* DEBUG
    	// Display the selection schema
    	Enumeration typeVals = selSchema.propertyNames();
    	while (typeVals.hasMoreElements() == true)
    	{
    		String field = (String)typeVals.nextElement();
    		System.out.println("SQLQueryStatement: key = " + field + " ; value = " + selSchema.getProperty(field));
    	}
    	*/
    	
		// Begin construction of the actual SQL statement
		theSQLStatement = "SELECT ";
		
		// add the fields from the schema, skip the tablename
		Enumeration fields = projectionSchema.propertyNames();
		while (fields.hasMoreElements() == true)
		{
			String field = (String)fields.nextElement();
			if(field.equals("TableName") != true)
			{
				// skip the leading comma if we're at the beginning
				if(theSQLStatement.length() > 7)
					theSQLStatement += ", " + field;
				else
					theSQLStatement += field;
			}
		}
		
		// add the tablename
		theSQLStatement += " FROM " + selSchema.getProperty("TableName");
		
		// Construct the WHERE part of the SQL statement
		String theWhereString = "";
		
		// Now, traverse the WHERE clause Properties object
		if (selectionValues != null)
		{
			Enumeration theWhereFields = selectionValues.propertyNames();
			while (theWhereFields.hasMoreElements() == true)
			{
				String theConjunctionClause = "";
				
				if (theWhereString.equals(""))
				{
		  			theConjunctionClause += " WHERE ";
				}
				else
				{
					theConjunctionClause += " AND ";
				}
	
				String theFieldName = (String)theWhereFields.nextElement();
				String theFieldValue = insertEscapes(selectionValues.getProperty(theFieldName));
				
				if (theFieldValue.equals("NULL"))
				{
					theWhereString += theConjunctionClause + theFieldName + " IS NULL";
				}
				else
				{
					// extract the type from the schema
					String actualType = selSchema.getProperty(theFieldName);
					
					// DEBUG: System.out.println("SQLQueryStatement: field = " + theFieldName + " ; type = " + actualType);
	
					
					if (actualType != null) 
					{
						// if the type is numeric, do NOT include quotes
						if (actualType.equals("numeric") == true)
						{
							if(theFieldValue.length() > 0)
								theWhereString += theConjunctionClause + theFieldName + " = " + theFieldValue;	// cannot partial match a numeric
						}
						else
						{
						
							// must the a text type 
							// first check if the value is a field name
							if (selSchema.containsKey(theFieldValue) == true)
							{
				
								theWhereString += theConjunctionClause + theFieldName + " = " + theFieldValue;	// two SQL variables are being compared	
							}
							else
							// else, it is an actual value, include the quotes
							// if theFieldValue is zero length, leave the quotes out (search for blank field)
							if (theFieldValue.length() > 0)
							{
								
								theWhereString += theConjunctionClause + theFieldName + " LIKE '" + theFieldValue + "%'";
							}
								
						}
						
					}
					else
					{
					
						theWhereString += theConjunctionClause + theFieldName + " = " + theFieldValue;	// two SQL variables are being compared
					}	

				}
			}
		}
		  
		theSQLStatement += theWhereString;
		
		theSQLStatement += ";";
		
		// DEBUG System.out.println("SQL Query Statement = " + theSQLStatement);
		
				
	}

}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLQueryStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.8  2004/01/25 06:43:54  smitra
//	A numeric value can also be from a string - so the empty string case has to
//	be handled in this case too
//	
//	Revision 1.7  2004/01/24 16:12:59  smitra
//	No changes - just debug stuff
//	
//	Revision 1.6  2004/01/16 23:14:48  smitra
//	Fix bugs in sentence construction
//	
//	Revision 1.5  2004/01/16 16:07:36  smitra
//	Fix error in the way empty and null fields are handled
//	
//	Revision 1.4  2003/11/14 14:11:46  tomb
//	Added test for empty fields to ensure proper detection.
//	
//	Revision 1.3  2003/10/28 06:09:16  smitra
//	Changed srcSchema to selSchema and destSchema to projectionSchema
//	
//	Revision 1.2  2003/10/26 18:36:09  tomb
//	Added destination schema to limit the number of fields returned by a query.
//	
//	Revision 1.1  2003/10/22 00:30:29  tomb
//	Initial Checkin.
//	
