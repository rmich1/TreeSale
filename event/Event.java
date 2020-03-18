// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/ATM/implementation/event/Event.java,v $
//
//	Reason: The destination class for events in the system.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: smitra $ */
/** @version	$Revision: 1.1 $ */

// specify the package
package event;

// system imports
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormat;

// project imports
import common.StringList;

/** 
 * The destination class for events in the system. The current 
 * implementation displays the events in a modal popup window.
 */
//==============================================================
public class Event
{
    // data members
    // Event level information
	public static final int DEBUG = 0;
	public static final int INFORMATION = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;
	public static final int FATAL = 4;
	public static final int NUMBER_SEVERITY_LEVELS = 5;
	
	// String values corresponding to the various event levels
	public static final String SeverityDescription[] =
	{
		"Debug",
		"Information",
		"Warning",
		"Error",
		"Fatal",
	};
	
	/**
	 * name of the source class
	 */
	String myClass;		
	
	/**
	 * Name of the method in which the event occurred
	 */
	String myMethod;			
	
	/**
	 * A description of the event
	 */
	String myDescription;	
	
	/**
	 * A timestamp for the event
	 */
	String myTime;			
	
	/**
	 * Tag used for internationalization of the event description
	 */
	String myTag;			
	
	/**
	 * A list of optional parameters that are embedded into the corresponding
	 * parameter points in the description. A typical description example is as follows:
	 * 
	 * "Failed to find file <0> in directory <1>".
	 *
	 * <0> and <1> are the two parameter points to be replaced by actual strings
	 * - typically <0> will be replaced by a file name and <1> by a directory name.
	 *
	 * This variable contains a comma-separated string with these actual parameters
	 */
	String myParams;		
	
	/**
	 * An ordinal severity level for this event
	 */
	int mySeverity;			
			

	/** 
	 * Class constructor with default tag 
	 *
	 * @param	source 	Name of class in which event occurred
	 *
	 * @param	name	Name of method in which event occurred
	 *
	 * @param	description	Plain English description of event details
	 *
	 * @param	severity	Ordinal severity level for event
	 */
	//----------------------------------------------------------
	public Event (String source, String name, String description, final int severity)
	{
		// forward to another constructor, provide defaults
		this(source, name, "NOTAG", null, description, severity);
	}

	/** 
	 * Class constructor with tag 
	 *
	 * @param	source 	Name of class in which event occurred
	 *
	 * @param	name	Name of method in which event occurred
	 *
	 * @param	tag		Tag used to refer to the Internationalized message description - which is 
	 *					pulled from the messages bundle
	 *
	 * @param	description	Plain English description of event details
	 *
	 * @param	severity	Ordinal severity level for event
	 *
	 */
	//----------------------------------------------------------
	public Event (String source, String name, String tag, String description, final int severity)
	{
		// forward to another constructor, provide defaults
		this(source, name, tag, null, description, severity);
	}
	
	/** 
	 * Class constructor with all options and parameters 
	 *
	 * @param	source 	Name of class in which event occurred
	 *
	 * @param	name	Name of method in which event occurred
	 *
	 * @param	tag		Tag used to refer to the Internationalized message description - which is 
	 *					pulled from the messages bundle
	 *
	 * @param	params	String which is a comma-separated list of parameters to fill in the parameter points
	 *					in the internationalized description tag with
	 *
	 * @param	description	Plain English description of event details
	 *
	 * @param	severity	Ordinal severity level for event
	 *
	 * @param		boolean indicating whether the event is synchronous or asynchronous
	 *
	 */
	//----------------------------------------------------------
	public Event (String source, String name, String tag, String params, String description, final int severity)
	{
		// create a timestamp
		Calendar rightNow = Calendar.getInstance(new Locale("en", "US"));
		myTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, new Locale("en", "US")).format(rightNow.getTime());

		myClass = source;	// save the source class name
		myMethod = name;		// and the method name
		myParams = params;	// save the parameters
		myDescription = description;	// save the description
		mySeverity = severity;		// save the severity
		myTag = tag;	// save the tag

		// add this event to the log
		EventLog.Instance().addEvent(this);
	}	
	
	/** 
	 * Provide the ordinal value of the severity level 
	 *
	 * @param	level	String value indicating the severity level
	 *
	 * @return	int value indicating the ordinal value of the severity level
	 */
	//----------------------------------------------------------
	public static int getSeverityMapping(String level)
	{
		// cruise through the list of severity descriptions
		// and see if we can find a match
		for (int cnt = 0; cnt < NUMBER_SEVERITY_LEVELS; cnt++)
			if (level.equals(SeverityDescription[cnt]) == true)
				return(cnt);

		// no match, return lowest level - should never happen
		return(0);
	}

	/** 
	  * Provide a complete displayable version of the Event.
	  * Note that this format is parsed by 
	  * EventLogModel.parseEvent() which will need to be changed
	  * if this format is modified 
	  *
	  * @return	String value providing a displayable form of the event in a format
	  *			which can be seen by looking at the code
	  */
	//----------------------------------------------------------
	public String toString()
	{
		return ("[" + myTime + "]"
			+ "|" + myClass
			+ "|" + myMethod
			+ "|" + SeverityDescription[mySeverity]
			+ "|" + myTag
			+ "|" + myParams
			+ "<" + myDescription + ">"
		);
	}


	/**
	 * Returns the class from which this event was generated
	 *
	 * @return	String object indicating class name
	 */
	//----------------------------------------------------------
	public String getSource() 
	{ 
		return myClass; 
	}
	
	/**
	 * Returns the parameter list associated with this event
	 *
	 * @return	String object indicating the comma-separated parameter list
	 */
	//----------------------------------------------------------
	public String getParameters() 
	{ 
		return myParams; 
	}

	/**
	 * Returns the internationalization tag associated with the event
	 *
	 * @return	String object indicating the internationalization tag
	 */
	//----------------------------------------------------------
	public String getTag() 
	{ 
		return myTag; 
	}

	/**
	 * Returns the method name from which this event was generated
	 *
	 * @return	String object indicating method name
	 */
	//----------------------------------------------------------
	public String getName() 
	{ 
		return myMethod; 
	}

	/**
	 * Returns the plain English description of this event
	 *
	 * @return	String object indicating the plain English description
	 */
	//----------------------------------------------------------
	public String getDescription() 
	{ 
		return myDescription; 
	}

	/**
	 * Returns a string containing the date and time of this event in US date format
	 *
	 * @return String object containing the date and time of this event in US date format
	 */
	//----------------------------------------------------------
	public String getTime()
	{
		// create a timestamp
		Calendar rightNow = Calendar.getInstance(new Locale("en", "US"));
		String timebuf = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("en", "US")).format(rightNow.getTime());
		return(timebuf);
	
	}

	/**
	 * Returns a String description of the severity level of this event
	 *
	 * @return	String object indicating the severity level of this event
	 */
	//----------------------------------------------------------
	public String getSeverityDesc()
	{ 
		return SeverityDescription[mySeverity]; 
	}

	/**
	 * Returns an ordinal value of the severity level of this event
	 *
	 * @return	int value indicating the severity level of this event
	 */
	//----------------------------------------------------------
	public int getSeverity()
	{ 
		return mySeverity; 
	}
	
	/**
	 * Method that is used to get the leaf-level class name of an Object
	 *
	 * @param	obj		Object whose leaf-level class name has to be obtained
	 *
	 * @return	String indicating leaf-level class name
	 */
	 //----------------------------------------------------------------
	 public static String getLeafLevelClassName(Object obj)
	 {
	 	String fullClassName = (obj.getClass()).getName();
	 	int lastIndexOfPeriod = fullClassName.lastIndexOf('.');
	 	if (lastIndexOfPeriod >= 0)
	 		{
	 			int fullClassNameLen = fullClassName.length();
	 			if ((lastIndexOfPeriod + 1) >= fullClassNameLen)
	 				return	fullClassName;
	 			else
	 				return fullClassName.substring(lastIndexOfPeriod + 1);
	 		}
	 	else
	 		return fullClassName;
	 }
	 
	 
	/** Not used */
	//----------------------------------------------------------
	public String getStatus()
	{
		return SeverityDescription[mySeverity];
	}
}


//**************************************************************
//	Revision History:
//				
//	$Log: Event.java,v $
//	Revision 1.1  2004/06/17 04:37:27  smitra
//	First check in
//	
