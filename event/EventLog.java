// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/ATM/implementation/event/EventLog.java,v $
//
//	Reason: Maintains a record of generated events in a log file.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: smitra $ */
/** @version	$Revision: 1.1 $ */

// specify the package
package event;

// system imports
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

// project imports
import common.StringList;

/** 
 * This class maintains the record of all generated events in a log file.
 * There are actually two log files - an 'actual' log file and a 'backup' 
 * log file. If the size of the actual log file exceeds 100K bytes, then the
 * contents of the actual log are transferred to the backup log, removing any
 * previously existing backup log. A new actual log file is started. The format
 * of a typical log file entry is as follows:
 *
 * [10/27/00 11:17 AM]|DatabaseAccessor|getMultiValuedSelectResult|Error|EVT_SQLException|null<The insert query could not be successfully completed>
 * 
 * Each of the sub-entries for each log file entry is separated from the previous by a separator (|).
 * We first have the date and time of the event, followed by an indication of the class in which the Event was generated,
 * followed by the name of the method in which it was generated, followed by the Severity level description,
 * followed by the tag which points to the Internationalized description, followed by the (possibly null) list of
 * parameters (which if it exists will be a comma-separated string), followed by the plain English description enclosed in
 * <...>. NOTE THAT THIS FORMAT IS IMPORTANT AND ANY CHANGE IN IT WILL AFFECT additional objects that parse
 * this string
 *
 * This class implements the SINGLETON pattern. Hence, there will only be one instance of EventLog
 */
//==============================================================
public class EventLog
{
    // data members
    /**
     * The single EventLog instance
     */
	private static EventLog instance;
	
	/**
	 * The name of the file holding the Event Log
	 */
	private static String LogFile = "ErrorLog.txt";
	
	/**
	 * The name of the file holding the Backup Log
	 */
	private static String Backup = "ErrorLogBackup.txt";
	
	/**
	 * Log file size limit
	 */
	private final static int MAX_SIZE = 100000;		
	
	/**
	 * Param length limit - to prevent long file names from stretching windows too long
	 */
	private final static int MAX_PARAM_LENGTH = 40;
	
	/**
	 * Delimiters for the plain English description
	 */
	private final static char OPEN_PLACEHOLDER = '<';
	private final static char CLOSE_PLACEHOLDER = '>';
	
	/**
	 * Indicates a level below which events are not to be reported/logged
	 */
	String reportingLevel;
	
	/**
	 * Messages bundle for Internationalization
	 */
	ResourceBundle myMessages;
		
	/**
	 * Flag that turns off/on the actual writing of the log. Sometimes it is necessary
	 * to disable writing (e.g., if the app using this class is running off a CD, or is
	 * running from an applet)
	 */
	private static boolean allowWrites = true;
	
	/**
	 * Flag that turns off/on the popup message to show the error. Will be turned
	 * off if this is running on the server side (for example).
	 */
	private static boolean allowPopupDisplay = false;
	
	
	/** Create and return an instance of EventLog. This method 
	 *  enforces a Singleton pattern. 
	 *
	 * @return	The single EventLog instance
	 */
	//----------------------------------------------------------
	public static EventLog Instance()
	{
		if (instance == null) 
		{
			instance = new EventLog();
		}
		return(instance);
	}

	/** 
	 * Class constructor, protected to enforce singleton 
	 */
	//----------------------------------------------------------
	protected EventLog ()
	{
		myMessages = null;
		reportingLevel = "Error";	// default reporting level 
		
		LogFile = "ErrorLog.txt";
		Backup = "ErrorLogBackup.txt";
				
		// see if the existing log file is too big 
		File logFile = new File(LogFile);
		if(logFile.length() > MAX_SIZE)
		{
			// since it's too big, delete the backup file (if it exists)
			File backupLog = new File(Backup);
			if(backupLog.exists() == true)
				backupLog.delete(); 
			
			// move the log file to the backup file
			if(logFile.renameTo(new File(Backup)) == false)
			{
				new Event(Event.getLeafLevelClassName(this), "EventLog", "Could not backup log file ", Event.WARNING);
			}
		}
	}

		
	/** 
	 * Provide the log file name to other interested parties 
	 *
	 * @return	String object representing the regular log file name
	 *
	 */
	//----------------------------------------------------------
	public static String getLogfileName()
	{
		return LogFile;
	}

	/** 
	 * Provide the backup log file name to other interested parties 
	 *
	 * @return	String object representing the backup log file name
	 */
	//----------------------------------------------------------
	public static String getBackupLogfileName()
	{
		return Backup;
	}

	/** 
	 * Disable logging events if we're on a non-writable media (CD-ROM) 
	 */
	//----------------------------------------------------------
	public void disallowLogFile()
	{
		allowWrites = false;
	}
	
	/** 
	 * Allow popup display (on client side applications for example)
	 */
	//----------------------------------------------------------
	public void allowPopupDisplay()
	{
		allowPopupDisplay = true;
	}
	/** 
	 * Set the messages bundle so we can translate the tags in the event
	 * content and display the translated description
	 *
	 * @param	messages	The Messages Bundle to use for Internationalization
	 */
	//----------------------------------------------------------
	public void setMessages(ResourceBundle messages)
	{
		myMessages = messages;
	}

	/** 
	 * Set the filtering level to prevent blasting the user with info. Only
	 * events at and above this set level will be recorded in the log
	 *
	 * @param	level	String representing desired severity level
	 */
	//----------------------------------------------------------
	public void setSeverityFilter(String level)
	{
		reportingLevel = level;
	}


	/** 
	 * Actual method called to add an event to the log file 
	 *
	 * @param	event	The Event object to record in the log
	 */
	//-------------------------------------------------------------
	public void addEvent(Event event)
	{

		// decide if this event should be shown to the user
		if((event.getSeverityDesc().equals("Warning") == true) ||
			(event.getSeverityDesc().equals("Information") == true) ||
			(event.getSeverityDesc().equals("Error") == true) ||
			(event.getSeverityDesc().equals("Fatal") == true))
		{

			if (allowPopupDisplay == true)
			{
				// If we don't have translations, display the raw text
				if(myMessages == null)
				{
					// The following statement puts up the popup
					JOptionPane.showMessageDialog(null, event.getSeverityDesc() + ": " + event.getDescription(),  "Error",
						JOptionPane.ERROR_MESSAGE);
				}
				else
				// if there are translations available, but this event doesn't support them, use raw text
				if (event.getTag().equals("NOTAG") == true)
				{
					// TBD
				}
				else
				{
					// use the translated text
					// TBD
				
				}
			}
		}

		// see if we need to write this to the log file
		if (event.getSeverity() >= Event.getSeverityMapping(reportingLevel))
		{
			// add it to the log file
			if (allowWrites) 
			{
				// we do this only if we are allowed to write 
				try 
				{
					FileOutputStream filestream = new FileOutputStream(LogFile, true);
					OutputStreamWriter outstream = new OutputStreamWriter(filestream);
					String eventstring = event.toString() + "\r\n";
					outstream.write(eventstring, 0, eventstring.length());
					outstream.close();
					filestream.close();
				}
				catch(Exception e)	
				{ 
					System.out.println(e);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * This method exists to enable the programmer to indicate whether sub-errors in
	 * the parameter processing should be displayed or not, via the third boolean
	 * parameter. This method, by default, checks for long file names. If this check is
	 * to be turned off, then the four-parameter version of this method has to be used
	 *
	 * @param	source	The actual description of the event (with embedded parameter points) that
	 *					has to be displayed in the popup
	 *
	 * @param	paramList	The comma-separated String indicating the parameter list that is used
	 *						to substitute the parameter points in the original description
	 *
	 * @param	displaySubErrors	boolean indicating whether to display sub-errors (generated in parsing description string)
	 *								or not
	 *
	 */
	//---------------------------------------------------------------
	public static String replaceParameters(String source, String paramList, boolean
	 	displaySubErrors)
	{
		return replaceParameters(source, paramList, displaySubErrors, true);
	}	
	
	/** 
	 * This method exists because it has to handle the fact that replaceParameters is called when the 
	 * Error Log table is redisplayed in EasyConfig as well. The invocation of replaceParameters
	 * in this context should not put up any popups for any internal errors it finds - hence, we 
	 * invoke the other replaceParameters directly from here, passing it a flag saying we do not
	 * want any sub-error popups. This method, which has only two parameters, is the default
	 * invocation which arranges for sub-error popups to be displayed. Note that this checks for
	 * long file names as well
	 *
	 * @param	source	The actual description of the event (with embedded parameter points) that
	 *					has to be displayed in the popup
	 *
	 * @param	paramList	The comma-separated String indicating the parameter list that is used
	 *						to substitute the parameter points in the original description
	 *
	 */
	//---------------------------------------------------------------
	public static String replaceParameters(String source, String paramList)
	{
		return replaceParameters(source, paramList, true, true);
	}
		
	/** 
	 * This method maps parameter point placeholders in internationalized description strings to parameters. It
	 * has a boolean parameter that indicates whether any sub-errors generated by this method (in parsing
	 * the description string) should be displayed or not
	 *
	 * @param	source	The actual description of the event (with embedded parameter points) that
	 *					has to be displayed in the popup
	 *
	 * @param	paramList	The comma-separated String indicating the parameter list that is used
	 *						to substitute the parameter points in the original description
	 *
	 * @param	displaySubErrors	boolean indicating whether to display sub-errors (generated in parsing description string)
	 *								or not
	 *
	 * @param	checkFileLength		boolean indicating whether to check for long file names or not
	 */
	//-------------------------------------------------------------
	public static String replaceParameters(String source, String paramList, boolean displaySubErrors,
		boolean checkFileLength)
	{
		String[] params;
		int numOfParams = 0;
		
		// Convert the comma-separated parameter string into a StringList and count the number
		// of elements it has
		StringList paramStringList = new StringList(paramList);
		while (paramStringList.hasMoreElements())
		{
			String dum = (String)paramStringList.nextElement();
			numOfParams++;
		}
		
		// Instantiate an array of that size to hold the extracted parameters
		params = new String[numOfParams];
		
		// Create another instance of a StringList from the original parameter string
		paramStringList = new StringList(paramList);
		
		// and go through it to extract its individual elements into the parameter array
		for (int cnt = 0; cnt < numOfParams; cnt++)
		{
			params[cnt] = (String)paramStringList.nextElement();
			if (checkFileLength == true)
				if (params[cnt].length() > MAX_PARAM_LENGTH) {
					params[cnt] = "..." + params[cnt].substring(params[cnt].length() - MAX_PARAM_LENGTH);
			}
		}
		
		
		int strlen = source.length();
		StringBuffer paramString = new StringBuffer();
		boolean done = false;
		int currentStringPosition = 0;

		// Loop through the description string to find the various parameter point placeholders and
		// replace them, one by one, from the parameter array created above		
		while (!done)
		{
			// find an indication of start of parameter point placeholder
			int posOfPlaceHolder = source.indexOf(OPEN_PLACEHOLDER, currentStringPosition);
		
			// if none, then we are done, simply append the rest of the description string - so that it may be returned
			if (posOfPlaceHolder < 0)
				{
					done = true;
					paramString.append(source.substring(currentStringPosition, strlen));
				}
			else
			{
				// Otherwise, find the position of the closing placeholder 
				int posOfClosePlaceHolder = source.indexOf(CLOSE_PLACEHOLDER, posOfPlaceHolder);
				// Put up an error in parsing if not found
				if (posOfClosePlaceHolder < 0)
				{
					done = true;
					if (displaySubErrors)
						new Event("EventLog", "String replaceParameters", "No close placeholder corresponding to open placeholder in source string "+source, Event.WARNING);
					// Append the rest of the description string - to return later
					paramString.append(source.substring(currentStringPosition, strlen));
				}
				else
				{
					// Otherwise, find the number that is enclosed within this parameter point
					String paramIndexStr = source.substring(posOfPlaceHolder+1, posOfClosePlaceHolder);
					int paramIndex = 0;
					String paramValue = null;
					try
					{
						paramIndex = Integer.parseInt(paramIndexStr);
					}
					catch (NumberFormatException ex )
						{
							// Display a parsing sub-error if not a good number
							done = true;
							if (displaySubErrors)
								new Event("EventLog", "String replaceParameters", "Specified parameter index "+paramIndexStr+" not an integer ", Event.WARNING);
							paramString.append(source.substring(currentStringPosition, strlen));				
						}
					try
					{
						// Get the actual parameter corresponding to this index from the parameter array.
						// NOTE : The actual description string could be of the form "xxx <1> yyy <0> zzz".
						// The number in the first placeholder thus could indicate that the 1th element from the
						// actual parameter list should be placed there and that the 0th element placed later - at
						// its point. Hence, this code is written the way and it is and it implies that ....
						paramValue = params[paramIndex];
					}
					// We need to check to see if the array index so included in the description string is a valid one or not
					// If not, we display a parsing sub-error and append the rest of the description string - to return later
					catch (ArrayIndexOutOfBoundsException ex)
						{
					
							done = true;
							if (displaySubErrors)
								new Event("EventLog", "String replaceParameters", "No parameter provided to correspond to index "+paramIndex, Event.WARNING);
							paramString.append(source.substring(currentStringPosition, strlen));				
						}
					if (!done)
					{
						// If all is OK, we append the description string till the beginning of the open placeholder position,
						// followed by an append of the actual parameter retrieved
						paramString.append(source.substring(currentStringPosition, posOfPlaceHolder)+paramValue);
						
						// And then we augment the pointer to the current string position - to consider in the next
						// iteration of the loop - to point just past the position of the close placeholder
						if (posOfClosePlaceHolder == strlen)
							{
								currentStringPosition = strlen;
								done = true;
							}
						else
							currentStringPosition = posOfClosePlaceHolder+1;
					}
				}
			}
		}
		
		// Return the final generated string with substituted parameters
		
		return paramString.toString();
	}

	

}


//**************************************************************
//	Revision History:
//				
//	$Log: EventLog.java,v $
//	Revision 1.1  2004/06/17 04:35:23  smitra
//	First check in
//	
//	Revision 1.3  2004/03/17 01:04:07  smitra
