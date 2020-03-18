// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/impresario/IControl.java,v $
//
//	Reason: The interface for a Control object in an MVC 
//			Impresario relationship.
//
//	Revision History: See end of file.
//
//*************************************************************

// JavaDoc information
/** @author		$Author: tomb $ */
/** @version	$Revision: 1.2 $ */

// specify the package
package impresario;

// system imports

// local includes

//==============================================================
public interface IControl
{
	// associate a Model to a Control object
	public void subscribe(String key,  IModel subscriber);		// for updating Models
	public void unSubscribe(String key, IModel subscriber);
}

//**************************************************************
//	Revision History:
//				
//	$Log: IControl.java,v $
//	Revision 1.2  2003/10/24 05:45:19  tomb
//	Changed register to subscribe.
//	
//	Revision 1.1  2003/10/24 04:11:18  tomb
//	Revamped interfaces to more closely follow the MVC architecture.
//	
//	Revision 1.1  2003/10/24 01:11:46  tomb
//	Initial Checkin.
//	
