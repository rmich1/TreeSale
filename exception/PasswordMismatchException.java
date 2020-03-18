// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/ATM/implementation/exception/PasswordMismatchException.java,v $
//
//	Reason: This class indicates an exception that is thrown 
//			if passwords don't match
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: smitra $ */
/** @version	$Revision: 1.1 $ */

// specify the package
package exception;

// system imports

// local imports

/** 
 * This class indicates an exception that is thrown if passwords
 * don't match
 * 
 */
//--------------------------------------------------------------
public class PasswordMismatchException
	extends Exception
{	
	/**
	 * Constructor with message
	 *
	 * @param The message associated with the exception
	 */
	//--------------------------------------------------------
	public PasswordMismatchException(String message)
	{
		super(message);
	}
}

		

//**************************************************************
//	Revision History:
//
//	$Log: PasswordMismatchException.java,v $
//	Revision 1.1  2004/06/18 20:36:26  smitra
//	First check in
//	
//	Revision 1.1  2004/06/17 04:40:56  smitra
//	First check in
//	
