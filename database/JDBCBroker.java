// tabs=4
//************************************************************
//	COPYRIGHT 2007 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$URL: svn://archsynergy.net/Volumes/Storage/SEEMTool/Implementation/database/JDBCBroker.java $
//
//	$Date: 2007-01-08 19:49:24 -0500 (Mon, 08 Jan 2007) $
//
//	Reason: Manage the connection to the database. This is a singleton!
//
//*************************************************************

/** @author		$Author: tomb $ */
/** @version	$Revision: 168 $ */
/** @version	$Revision: timmullins,2008-02-20 $ */

// specify the package
package database;

/// system imports
import java.util.Enumeration;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

// project imports
import common.PropertyFile;
import event.Event;

//==============================================================
public class JDBCBroker
{
    // Single broker to be shared by all other Servlets
 	private static JDBCBroker myInstance = null;
    	private static Connection theDBConnection = null;
	public static Driver theDriver = null;
	private PropertyFile props = null;

	// DB Access data
	private String dbName = null;
	private String username = null;
	private String password = null;
    private String server = null;
    
	// singleton constructor
	//----------------------------------------------------------
	static public JDBCBroker getInstance()
	{
		// DEBUG: System.out.println("JDBCBroker.getInstance()");
		
	        if(myInstance == null)
        	{
			myInstance = new JDBCBroker();
		}

		/* DEBUG
     		Enumeration e = DriverManager.getDrivers();
     		while (e.hasMoreElements())  
     		{
     			Driver d = (Driver)e.nextElement();
     			//theDriver = (Driver)e.nextElement();
     			System.out.println("Driver Major Version = " + 
     			 d.getMajorVersion());
     			//theDriver.getMajorVersion());   	
 	        } */  
		
		return myInstance;
	}
	
	
	// private constructor for singleton
	//----------------------------------------------------------
	protected JDBCBroker()
    	{
    		// DEBUG: System.out.println("JDBCBroker.JDBCBroker()");
		props = new PropertyFile("dbConfig.ini");
		if (props != null)
		{
			dbName = props.getProperty("dbName");
			username = props.getProperty("username");
			password = props.getProperty("password");
			server = props.getProperty("server");
			if (server == null)
				server = "localhost";
		}
		String driverClassName = "com.mysql.jdbc.Driver";
		try
		{	
			// load and register the JDBC driver classes
			theDriver = (Driver) Class.forName(driverClassName).newInstance();
		} 
			catch(ClassNotFoundException exc)
		{
			System.err.println("JDBCBroker.JDBCBroker - Could not load driver class: ClassNotFoundException");
			new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
		}	
			catch(InstantiationException exc)
		{
			System.err.println("JDBCBroker.JDBCBroker - Could not load driver class: InstantiationException");
			new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
		}
			catch(IllegalAccessException exc)
		{
			System.err.println("JDBCBroker.JDBCBroker - Could not load driver class: IllegalAccessException");
			new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);			
    		}
 	}
    
	/** Create a connection to the database */
	//-------------------------------------------------------- 
	public Connection getConnection() 
	{	
		//System.out.println("JDBCBroker.getConnection() with Driver " + theDriver);
		if (myInstance != null)
		{
			if(theDBConnection == null)
			{
				if ((dbName != null) & (username != null) && (password != null))
				{
					try
					{
						// Create a connection to the database
						theDBConnection = theDriver.connect("jdbc:mysql://"+server+":3306/" + 
							dbName + "?" + "user=" + username + "&password=" +
							password, null);					
					}
					catch(SQLException exc)
					{
						System.err.println("JDBCBroker.getConnection - Could not connect to database!" + "\n" + exc.getMessage());
						//new Event(Event.getLeafLevelClassName(this), "getConnection", "Could not connect to database", Event.ERROR);
					}     
				}
			}   
		}	
		//System.out.println("JDBCBroker.getConnection() with connection " + theDBConnection);
	       	return theDBConnection;
    }
    
    
	/** Release a previously allocated connection */
	//--------------------------------------------------------
	public void releaseConnection(Connection connection) 
	{
		// don't release the connection, hang on till we're destructed
	}
	
	//--------------------------------------------------------
	protected void finalize()
	{
		if(theDBConnection != null)
        {
			try
			{
				theDBConnection.close();
				theDBConnection = null;
			}
			catch(SQLException exc)
			{
				new Event(Event.getLeafLevelClassName(this), "releaseConnection", "Could not release connection", Event.WARNING);
			}        
		}
	}
}
