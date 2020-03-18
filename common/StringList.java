// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/ATM/implementation/common/StringList.java,v $
//
//	Reason: Provide an enumeration interface over a delimited 
//			String.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: smitra $ */
/** @version	$Revision: 1.1 $ */

// specify the package
package common;

// system imports
import java.util.Enumeration;

// local imports

/** This class handles a string which contains embedded strings. 
 *  The enumeration interface is implemented to provide access 
 *  to string elements.	The tokens are assumed to be separated 
 *  by commas (no spaces).
 */
//--------------------------------------------------------------
public class StringList 
	implements Enumeration
{
	/** The delimeter for elements in the list */
	private char delimiter = ',';
	/** Reference to the string */
	private String myString = null;		
	/** The current token */
	private String myToken = null;		
	/** Index bounds of current token */
	private int start = 0, end = 0;		

	/** Construct a StringList from a comma-delimeted string 
	 *
	 * @param	str		The comma-separated String to convert to an object of this type
	 */
	//----------------------------------------------------------
	public StringList(String str)
	{
		// assign the reference
		myString = str;
		// initialize the token indexes
		//delimiter = ',';
		start = 0;
		end = 0;
	}

	/** Construct a StringList from a potentially non-comma-delimeted string 
	 *
	 * @param	str		The comma-separated String to convert to an object of this type
	 * @param	del		delimiter used for parsing the list
	 */
	//----------------------------------------------------------
	public StringList(String str, char del)
	{
		// assign the reference
		myString = str;
		// set the delimiter
		delimiter = del;
		// initialize the token indexes
		start = 0;
		end = 0;
	}

	/** Return the length of the string 
	*
	* @return	int value returning the length of the string
	*/
 	//----------------------------------------------------------
	public int length()
	{
		if (myString == null)
			return 0;
		else
			return myString.length();
	}
	
	
	/** Return the string version of our string(?) */
 	//----------------------------------------------------------
	public String toString()
	{
		return myString;
	}

	/** Determine if we have any more elements in our list 
	 *
	 * @return	boolean value indicating whether there are more elements in the list (or not)
	 */
	//----------------------------------------------------------
	public boolean hasMoreElements()
	{
		if(myString != null)
			return(start < myString.length());

		return false;
	}


	/** Return the next element in our list 
	 *
	 * @return	Object indicating the next element retrieved from list
	 */
	//----------------------------------------------------------
    public Object nextElement()
	{
		myToken = null;

		if(myString != null)
		{
			end = myString.indexOf(delimiter, start);
			// make sure we handle the end of the string correctly
			if(end < 0)
				end = myString.length();
			myToken = myString.substring(start, end);
			start = end + 1;
		}
		return myToken;
	}
}

		

//**************************************************************
//	Revision History:
//
//	$Log: StringList.java,v $
//	Revision 1.1  2004/06/17 04:31:41  smitra
//	First check in
//	
