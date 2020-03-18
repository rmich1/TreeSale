// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLSelectStatement.java,v $
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
public class SQLSelectStatement extends SQLStatement
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
    public SQLSelectStatement(Properties schema,
    						  Properties whereValues)
    {
		// Begin construction of the actual SQL statement
		theSQLStatement = "SELECT ";
		
		// add the fields from the schema, skip the tablename
		Enumeration fields = schema.propertyNames();
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
		theSQLStatement += " FROM " + schema.getProperty("TableName");
		
		// Construct the WHERE part of the SQL statement
		String theWhereString = "";
	
		// Now, traverse the WHERE clause Properties object
		if (whereValues != null)
		{
			Enumeration theWhereFields = whereValues.propertyNames();
			while (theWhereFields.hasMoreElements() == true)
			{
				
				String theFieldName = (String)theWhereFields.nextElement();
				String theFieldValue = insertEscapes(whereValues.getProperty(theFieldName));
				
				if (theFieldValue.length() > 0)		// Exclude empty strings
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
	
					if (theFieldValue.equals("NULL"))
					{
						theWhereString += theConjunctionClause + theFieldName + " IS NULL";
					}
					else
					{
						// extract the type from the schema
						String actualType = schema.getProperty(theFieldName);
	
						// if the type is numeric, do NOT include quotes.
						if ((actualType != null) && (actualType.equals("numeric") == true))
						{
							theWhereString += theConjunctionClause + theFieldName + " = " + theFieldValue;
						}
						else
						{
							// must the a text type, include the quotes.
							theWhereString += theConjunctionClause + theFieldName + " = '" + theFieldValue + "'";
						}
					}	

				}
			}
		}
		  
		theSQLStatement += theWhereString;
		
		// DEBUG: System.out.println(theSQLStatement);
		
		theSQLStatement += ";";
				
	}

}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLSelectStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.5  2004/04/23 14:51:33  smitra
//	Changed the code to reflect the workflow in Persistable
//	
//	Revision 1.4  2004/01/25 06:43:17  smitra
//	Fix bug that needed to eliminate empty values from SQL statement constructed.
//	Also, a numeric value can also be from a string - so the empty string case has to
//	be handled in this case too
//	
//	Revision 1.3  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//	
//	Revision 1.2  2003/10/03 20:04:36  tomb
//	Rewrote statement creation algorithm to accomodate new parameters.
//	
//	Revision 1.1  2003/10/01 01:21:37  tomb
//	Initial checkin, reflects behavior extracted from EasyVideo DatabaseMutator and Accessor.
//	
