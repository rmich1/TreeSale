// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLStatement.java,v $
//
//	Reason: Represents an abstract SQL Statement that can be 
//			applied to a database.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: pwri0503 $ */
/** @version	$Revision: 1.1.1.2 $ */


// specify the package
package database;

// system imports

// project imports

// Beginning of DatabaseManipulator class
//---------------------------------------------------------------------------------------------------------
public abstract class SQLStatement
{
	// constants used for parsing statements
	private final char characterToEscape = '\'';
	private final String escapeString = "\\";
	protected String theSQLStatement;		// contains the resulting SQL statement
    

	/**
     * In order to facilitate having apostrophe's in the data in the DB table
     * columns, we need to insert the '\\' as escape string in the data values. This method
     * accomplishes that.
     * 
     */
	//----------------------------------------------------------------------
	protected String insertEscapes(String inString)
	{
		// define our local data and constants
		String outString = "";
		int inStringLen = inString.length();
		int indexOfEscapeChar = inString.indexOf(characterToEscape);
		boolean allDone = (indexOfEscapeChar == -1);

		while (allDone == false) // in other words, there is still an escape char to handle
		{
			String prefix = inString.substring(0, indexOfEscapeChar);
			outString += prefix;
			outString += escapeString;
			outString += inString.charAt(indexOfEscapeChar);
			
			if (indexOfEscapeChar + 1 >= inStringLen)
			{
				allDone = true;
				inString = "";
			}
			else
			{
				inString = inString.substring(indexOfEscapeChar + 1);
				indexOfEscapeChar = inString.indexOf(characterToEscape);
				allDone = (indexOfEscapeChar == -1);
			}
		} // while

		outString += inString;
		
		return outString;
	}
	
	
	// override the toString method to output the constructed string
	//----------------------------------------------------------
	public String toString()
	{
		return theSQLStatement;
	}

}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.3  2003/10/01 01:20:46  tomb
//	Converted to abstract base class.
//	
//	Revision 1.2  2003/09/14 23:32:08  tomb
//	Changed to use filedList.
//	
//	Revision 1.1  2003/09/07 21:15:01  tomb
//	Initial checkin, extracted from Persistable.java.
//	
