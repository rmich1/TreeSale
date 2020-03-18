// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/impresario/IView.java,v $
//
//	Reason: The interface for a View object in an MVC
//			Impresario relationship.
//
//	Revision History: See end of file.
//
//*************************************************************

// JavaDoc information
/** @author		$Author: tomb $ */
/** @version	$Revision: 1.1 $ */

// specify the package
package impresario;

// system imports

// local includes

//==============================================================
public interface IView
{
	// allows a Model to update our visual state
	public void updateState(String key, Object value);

}

//**************************************************************
//	Revision History:
//				
//	$Log: IView.java,v $
//	Revision 1.1  2003/10/24 04:11:18  tomb
//	Revamped interfaces to more closely follow the MVC architecture.
//	
//	Revision 1.1  2003/10/24 01:11:46  tomb
//	Initial Checkin.
//	
