// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/impresario/Registry.java,v $
//
//	Reason: The base class for the different Registry classes, 
//			includes the registry hastable and subscription methods.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: smitra $ */
/** @version	$Revision: 1.12 $ */

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
public abstract class Registry
{
    // data members
    /** A list of subscribers for each key of interest */
	protected Hashtable mySubscribers;
	protected String myClassName;
		

	// Class constructor
	//----------------------------------------------------------
	public Registry(String classname) 	// the name of the class that contains this Registry, debug only
	{
		myClassName = classname;	// remember our classname for debug messages

		// create an object to hold our subscriber list
		mySubscribers = new Hashtable();

	}
		

	/** 
	 * Method that is used to associate (subscribe) a particular object (Model, View) to a key. 
	 * After this method executes, the Registry object knows that the subscriber object 
	 * is associated with the key, and when an update to the key must be invoked, the subscriber
	 * object is one of the actual objects whose update method must be called
	 *
	 * @param	key		String indicating key to which the subscriber object wants to subscribe
	 *
	 * @param	subscriber	Object that wishes to subscribe to the key
	 */
	//----------------------------------------------------------
	public void subscribe(String key, Object subscriber)
	{
		// debug only, doesn't need translation
		// DEBUG: if (key.equals("dbDateDue")) System.out.println("Registry.subscribe - (" + key + ", " + subscriber.getClass() + ")");		
		
		if(mySubscribers.containsKey(key) == true)
		{
			// pull the current object and see if it's a Vector
			Object tempObj = mySubscribers.get(key);
			if(tempObj instanceof Vector)
			{
				// SANDEEP: FIRST CHECK IF subscriber IS ALREADY IN THIS Vector. IF SO, DON'T ADD subscriber
				// add this object to the existing list - i.e.,
				// if (((Vector)tempObj).contains(subcriber) == false)
				((Vector)tempObj).addElement(subscriber);
			}
			else // if not a Vector, must be a single subscriber so convert to a list
			{
				// SANDEEP: FIRST CHECK IF subscriber AND tempObj ARE THE SAME. IF SO, DON'T ADD subscriber  
				// create a new vector to hold our subscriber list
				Vector tempVector = new Vector();
				// add the original subscriber and the new one
				tempVector.addElement(tempObj);
				tempVector.addElement(subscriber);
				
				// remove the original key and subscriber from the hashtable
				mySubscribers.remove(key);
				mySubscribers.put(key, tempVector);
			}
		}
		else
		{
			// install as a single subscriber if not a Vector
			if (subscriber instanceof Vector)
			{
				// create a new vector to hold our subscriber list
				Vector tempVector = new Vector();
				tempVector.addElement(subscriber);
				mySubscribers.put(key, tempVector);
					
			}
			else
			{
				mySubscribers.put(key, subscriber);
			}
		}
	}
	

	/** 
	 * Unassociate the subscriber object from the key
	 *
	 * @param	key		Key value to unassociate from subscriber
	 *
	 * @param	subscriber	Model or View to unassociate from key
	 */
	//----------------------------------------------------------
	public void unSubscribe(String key, Object subscriber)
	{
		// debug only, doesn't need translation
//		new Event(Event.getLeafLevelClassName(this), "unSubscribe", "Unsubscribe key " + key + " for " + subscriber.getClass(), Event.DEBUG);

		// make sure this is a valid request
		if(mySubscribers.containsKey(key) == true)
		{
			// pull the current object and see if it's a Vector
			Object tempObj = mySubscribers.get(key);
			if(tempObj instanceof Vector)
			{
				// remove this object from the existing list
				((Vector)tempObj).removeElement(subscriber);
			}
			else // if not a Vector, must be a single subscriber so just remove it
			{
				// remove the original key and subscriber from the hashtable
				mySubscribers.remove(key);
			}
		}
	}

}

//**************************************************************
//	Revision History:
//
//	$Log: Registry.java,v $
//	Revision 1.12  2004/02/13 20:35:11  smitra
//	Added code comment about how to prevent dup registration of same object
//	under same key
//	
//	Revision 1.11  2004/01/31 02:13:39  smitra
//	made a comment about duplicate subscriptions to same key
//	
//	Revision 1.10  2004/01/18 05:51:32  smitra
//	Fixed the bug when a view registered is a Java Vector
//	
//	Revision 1.9  2003/10/24 17:45:32  tomb
//	Fixed base class name from Registry1 to Registry.
//	
//	Revision 1.8  2003/10/24 17:32:54  tomb
//	New base class for Registry files.
//	
//	Revision 1.1  2003/10/24 12:32:02  tomb
//	Temporary file, will be removed shortly.
//	
//	Revision 1.6  2003/10/10 14:48:58  tomb
//	Removed debug statement.
//	
//	Revision 1.5  2003/10/08 17:18:48  tomb
//	Fixed a bug that shortcut cascading notifications.
//	
//	Revision 1.4  2003/10/07 17:09:55  tomb
//	Updated to new Impresario implementation.
//	
//	Revision 1.3  2003/09/17 17:34:15  tomb
//	Removed debug statements.
//	
//	Revision 1.2  2003/09/15 15:45:34  tomb
//	Updated to reflect name changes and refactoring for StateSource and StateCallback.
//	
//	Revision 1.1  2003/09/15 14:08:20  tomb
//	Initial Checkin. copied from EasyVideo.
//	
//	Revision 1.4  2003/09/14 23:29:11  tomb
//	Added empty constructor, added ItemChangeable to updateSubscribers.
//	
//	Revision 1.3  2003/08/06 13:56:53  smitra
//	New interface ... more updated
//	
//	Revision 1.2  2003/08/05 22:13:09  smitra
//	New interface
//	
//	Revision 1.1  2003/07/25 19:01:21  smitra
//	Re-package to include classes of ArchSynergy's own architecture pattern in this directory
//	
//	Revision 1.2  2003/07/24 04:24:41  tomb
//	Changed packaging.
//	
//	Revision 1.1  2003/07/17 00:26:24  tomb
//	Initial Checkin
//	
