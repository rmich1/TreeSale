// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/impresario/ControlRegistry.java,v $
//
//	Reason: The registry mechanism for the Control object in
//			an MVC relationship.
//
//	Revision History: See end of file.
//
//*************************************************************

// JavaDoc information
/** @author		$Author: tomb $ */
/** @version	$Revision: 1.4 $ */

// specify the package
package impresario;

// system imports
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;

// project imports
import common.PropertyFile;
import common.StringList;

import event.Event;

/** 
 * This class is used to instantiate the object that is encapsulated
 * by every EasyObserver client in order to keep track of which control
 * subscribes to which key and which keys depend on which other keys.
 * After the client updates its state on the basis of a posted state change,
 * this class' methods are used to update the GUI controls that subscribe to
 * the keys that depend on the key on which the state change is posted.
 */
//==============================================================
public class ControlRegistry extends Registry
{
    // data members

	// Class constructor
	//----------------------------------------------------------
	public ControlRegistry(String classname) 	// the name of the class that contains this Registry, debug only
	{
		super(classname);	// construct our base class
	}


	/**
	 * This method invokes the stateChangeRequest method on all IModels that 
	 * have subscribed for the specified key.
	 *
	 * @param	key		The key on which the state change 
	 *
	 * @param	value	The value of the key that has changed
	 */
	//----------------------------------------------------------
	public void updateSubscribers(String key, Object value)
	{
		// DEBUG: System.out.println("ControlRegistry.updateSubscribers - " + key + ", value " + value);
			
		// Get all subscribers to this key
		Object tempObj = mySubscribers.get(key);
		
		// make sure we have subscribers
		if(tempObj == null)
		{
			// DEBUG: System.out.println("ControlRegistry[" + myClassName + "].updateSubscribers - no subscribers found for dependency " + dependProperty);
			return;
		}
			
		// see if we have multiple subscribers
		if(tempObj instanceof Vector)
		{
			// get the list of elements
			Enumeration subscriberList = ((Vector)tempObj).elements();				
			while(subscriberList.hasMoreElements() == true) 
			{
				// extract each subscriber
				Object subscriber = subscriberList.nextElement();
				// DEBUG: System.out.println("Vector Subscriber: " + subscriber.getClass());

        		// update via a key-value pair
				if(subscriber instanceof IModel)
        		{
					// DEBUG: System.out.println("Vector StateRepository [" + key + "] " + dependProperty + ": " + client.getValue(dependProperty));
        			((IModel)subscriber).stateChangeRequest(key, value);
        		}
        		else 
        		{
					new Event(Event.getLeafLevelClassName(this), "UpdateSubscribers", "EVT_InvalidSubscriber", "Vector Invalid Subscriber: " + subscriber.getClass(), Event.WARNING);
					System.err.println("ControlRegistry.updateSubscribers - Invalid Subscriber type!");
				}
			}
		}
		else	// we must have a single subscriber	
        // If not, use the standard update via a key-value pair
		if(tempObj instanceof IModel)
		{
			// DEBUG: System.out.println("Changeable [" + key + "] " + dependProperty + ": " + client.getValue(dependProperty));
        	((IModel)tempObj).stateChangeRequest(key, value);
		}
        else 
        {
			new Event(Event.getLeafLevelClassName(this), "UpdateSubscribers", "EVT_InvalidSubscriber", "Invalid Subscriber: " + tempObj.getClass(), Event.WARNING);
			System.err.println("ControlRegistry.updateSubscribers - Invalid Subscriber type!");
		}
	}			
}


//**************************************************************
//	Revision History:
//
//	$Log: ControlRegistry.java,v $
//	Revision 1.4  2003/10/24 17:45:32  tomb
//	Fixed base class name from Registry1 to Registry.
//	
//	Revision 1.3  2003/10/24 12:31:43  tomb
//	Updated to use Registry1 as a base class.
//	
//	Revision 1.2  2003/10/24 05:45:19  tomb
//	Changed register to subscribe.
//	
//	Revision 1.1  2003/10/24 04:12:07  tomb
//	The original Registry class has been split into ModelRegistry and Controlregistry to better reflect their behavrio relative to the new MVC Impresario interfaces.
//	
