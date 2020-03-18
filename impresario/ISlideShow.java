// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/impresario/ISlideShow.java,v $
//
//	Reason: The interface for the entity that is responsible 
//			for switching views in an MVC setup.
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
public interface ISlideShow
{
	
	/** Swap to the view indicated by the String parameter */
	public void swapToView(IView viewName);
}

//**************************************************************
//	Revision History:
//				
//	$Log: ISlideShow.java,v $

