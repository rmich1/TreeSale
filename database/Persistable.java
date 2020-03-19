// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/Persistable.java,v $
//
//	Reason: The base class for classes requiring persistence.
//
//	Revision History: See end of file.
//
//*************************************************************

/** @author		$Author: pwri0503 $ */
/** @version	$Revision: 1.1.1.2 $ */


// specify the package
package database;

// system imports
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;


// project imports
import event.Event;


// Beginning of DatabaseManipulator class
//---------------------------------------------------------------------------------------------------------
abstract public class Persistable
{

    // local state variables
    private JDBCBroker myBroker = null;		// database connection broker

    protected boolean available = false;		// whether or not we're available
    private static final int MAX_ROWS = 20000;

    private Statement theStatement = null;
    private Connection theDBConnection = null;

//    protected String persistableStatus;

   // class constructor
   //------------------------------------------------------------
    protected Persistable()
    {

		// DEBUG System.out.println("Persistable.");

		// initialize our state
		myBroker = JDBCBroker.getInstance();
				
		available = true;
	
	
		// DEBUG System.out.println("Leaving Persistable constructor.");
    }

   /**
     * Create a Properties object representing aspects of the
     * 'schema' of a table - namely, the column names and the types
     * of the columns
     *
     * @param   tableName name to get schema information for
     *
     * @return Properties object indicating column names as keys and column
     *         types as values
     */
    //------------------------------------------------------------
    protected Properties getSchemaInfo(String tableName)
    {	
    	// DEBUG System.out.println("Persistable.getSchemaInfo for table " + tableName);
    	try
		{
		// Create a connection to the database
		Connection theDBConnection = myBroker.getConnection();
			
		/* System.out.println("Persistable.getSchemaInfo(..) connection = " + theDBConnection); */

		// extract the metadata from the database
		DatabaseMetaData dbMetaData = theDBConnection.getMetaData();

    	// create a place to hold our return information
    	Properties valToReturn = new Properties();
    	// add the name of our table to the return props
    	valToReturn.setProperty("TableName", tableName);

    	// get the names of the columns from the database
    	ResultSet columnInfo = dbMetaData.getColumns(null, null, tableName, null);
    	while (columnInfo.next())
    	{
    		// DEBUG System.out.println("Column Name = " + columnInfo.getString(4) + ", Column Type = " + columnInfo.getString(6));
    		String typeValue = columnInfo.getString(6);

			typeValue = typeValue.toLowerCase();
    		if ((typeValue.startsWith("smallint") == true) || (typeValue.startsWith("mediumint") == true) ||
    			(typeValue.startsWith("int") == true))
    		{
    			typeValue = "numeric";
   			}
    		else
			{
    				typeValue = "text";
    		}

    		// add the column / field name and type to the return props
    		valToReturn.setProperty(columnInfo.getString(4), typeValue);
    	}   		

		columnInfo.close();

    	return valToReturn;
		}	
		catch (SQLException sqle)
		{
//			DEBUG: System.err.println( "An SQL Error Occured:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "getSchemaInfo", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			return null;
		}
    }


    /**
     * Create and execute a SQL statement to extract the required
     * fields from the database.
     * Returns a Vector with each element being a Properties object
     * containing the columnName=columnValue mappings
     */
    //------------------------------------------------------------
    protected Vector getPersistentState(Properties schema,
					Properties where)
    {
		int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet
		ResultSet theResultSet = null;			// the resultset from the SQLStatement execution

		try
		{
			// connect to the database
			//new Event(Event.getLeafLevelClassName(this), "getPersistentState", "Attempting to get database connection", Event.DEBUG);

			theDBConnection = myBroker.getConnection();

			//new Event(Event.getLeafLevelClassName(this), "getPersistentState", "Return from database connection attempt call",
			//	Event.DEBUG);

			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.getPersistentState - Could not connect to database!");
				return null;
			}

	    	// construct a SQL statement from the passed parameters
			SQLSelectStatement theSQLStatement = new SQLSelectStatement(schema, where);

			// DEBUG System.out.println("SQL Statement: " + theSQLStatement.toString());

			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.err.println("Persistable.getPersistentState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(MAX_ROWS);

			// The method executeQuery executes a query on the database. The
			// return result is of type ResultSet which is one or more rows in
			// this case.
			theResultSet = theStatement.executeQuery(theSQLStatement.toString());
			// verify the results
			if (theResultSet == null)
			{
				System.err.println("Persistable.getPersistentState - Invalid result set from SQL statement!");
				return null;
			}

			// get the column information from the ResultSet
			ResultSetMetaData rsMetaData = theResultSet.getMetaData();

			numRSColumns = rsMetaData.getColumnCount();
			namesRSColumns = new Vector();
			for (int cnt = 1; cnt <= numRSColumns; cnt++)
			{
				String thisColumnName = rsMetaData.getColumnName(cnt);
				namesRSColumns.addElement(thisColumnName);
			}

			Vector resultSetToReturn = new Vector();

			while (theResultSet.next() == true)
			{
				Properties thisRow = new Properties();
				for (int cnt = 1; cnt <= numRSColumns; cnt++)
				{
					String theColumnName = (String)namesRSColumns.elementAt(cnt-1);
					String theColumnValue = theResultSet.getString(cnt);

					// The value of the column might be NULL. In that case, we DON'T
					// put it into the Properties object
					if (theColumnValue != null)
					{
						thisRow.setProperty(theColumnName, theColumnValue);
					}
				}
				resultSetToReturn.addElement(thisRow);
			}			

			if (theResultSet != null)
				theResultSet.close();	
			return resultSetToReturn;
		}
		catch (SQLException sqle)
		{
//			DEBUG: System.err.println( "An SQL Error Occurred:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			
			new Event(Event.getLeafLevelClassName(this), "getPersistentState", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			return null;
		}
		finally
		{
			closeStatement();
		}
	}


    /**
     * Create and execute a SQL statement to extract the required
     * fields from the database.
     * Returns a Vector with each element being a Properties object
     * containing the columnName=columnValue mappings
     */
    //------------------------------------------------------------
    protected Vector getQueriedState(Properties selSchema,
    								 Properties projectionSchema,
									 Properties where)
    {
		int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet
		ResultSet theResultSet;			// the resultset from the SQLStatement execution

		try
		{
			// connect to the database
			theDBConnection = myBroker.getConnection();
			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.getQueriedState - Could not connect to database!");
				return null;
			}

	    	// construct a SQL statement from the passed parameters
			SQLQueryStatement theSQLStatement = new SQLQueryStatement(selSchema, projectionSchema, where);

			// DEBUG: System.out.println("SQLQueryStatement: " + theSQLStatement.toString());

			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.err.println("Persistable.getQueriedState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(20000);

			// The method executeQuery executes a query on the database. The
			// return result is of type ResultSet which is one or more rows in
			// this case.
			theResultSet = theStatement.executeQuery(theSQLStatement.toString());
			// verify the results
			if (theResultSet == null)
			{
				System.err.println("Persistable.getQueriedState - Invalid result set from SQL statement!");
				return null;
			}

			// get the column information from the ResultSet
			ResultSetMetaData rsMetaData = theResultSet.getMetaData();

			numRSColumns = rsMetaData.getColumnCount();
			// DEBUG: System.out.println("Persistable.getColumnCount is " + numRSColumns);
			namesRSColumns = new Vector();
			for (int cnt = 1; cnt <= numRSColumns; cnt++)
			{
				String thisColumnName = rsMetaData.getColumnName(cnt);
				namesRSColumns.addElement(thisColumnName);
			}

			Vector resultSetToReturn = new Vector();

			while (theResultSet.next() == true)
			{
				Properties thisRow = new Properties();
				for (int cnt = 1; cnt <= numRSColumns; cnt++)
				{
					String theColumnName = (String)namesRSColumns.elementAt(cnt-1);
					String theColumnValue = theResultSet.getString(cnt);

					// The value of the column might be NULL. In that case, we DON'T
					// put it into the Properties object
					if (theColumnValue != null)
					{
						thisRow.setProperty(theColumnName, theColumnValue);
					}
				}
				resultSetToReturn.addElement(thisRow);
			}
			
			if (theResultSet != null)
				theResultSet.close();
			return resultSetToReturn;
		}
		catch (SQLException sqle)
		{
//			DEBUG: 
			System.err.println( "Persistable.getQueriedState: An SQL Error Occurred:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "getQueriedState", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			return null;
		}
		finally
		{
				
			closeStatement();
		}
	}

 	/**
     * Create and execute a SQL statement to extract the required
     * fields from the database.
     * Returns a Vector with each element being a Properties object
     * containing the columnName=columnValue mappings
     */
    //------------------------------------------------------------
    protected Vector getQueriedStateWithExactMatches(Properties selSchema,
    								 Properties projectionSchema,
									 Properties where)
    {
		int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet
		ResultSet theResultSet;			// the resultset from the SQLStatement execution

		try
		{
			// connect to the database
			theDBConnection = myBroker.getConnection();
			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.getQueriedState - Could not connect to database!");
				return null;
			}

	    	// construct a SQL statement from the passed parameters
			SQLQueryStatementWithExactMatches theSQLStatement = 
				new SQLQueryStatementWithExactMatches(selSchema, projectionSchema, where);

			// DEBUG: System.out.println("SQLQueryStatement: " + theSQLStatement.toString());

			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.err.println("Persistable.getQueriedState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(20000);

			// The method executeQuery executes a query on the database. The
			// return result is of type ResultSet which is one or more rows in
			// this case.
			theResultSet = theStatement.executeQuery(theSQLStatement.toString());
			// verify the results
			if (theResultSet == null)
			{
				System.err.println("Persistable.getQueriedState - Invalid result set from SQL statement!");
				return null;
			}

			// get the column information from the ResultSet
			ResultSetMetaData rsMetaData = theResultSet.getMetaData();

			numRSColumns = rsMetaData.getColumnCount();
			namesRSColumns = new Vector();
			for (int cnt = 1; cnt <= numRSColumns; cnt++)
			{
				String thisColumnName = rsMetaData.getColumnName(cnt);
				namesRSColumns.addElement(thisColumnName);
			}

			Vector resultSetToReturn = new Vector();

			while (theResultSet.next() == true)
			{
				Properties thisRow = new Properties();
				for (int cnt = 1; cnt <= numRSColumns; cnt++)
				{
					String theColumnName = (String)namesRSColumns.elementAt(cnt-1);
					String theColumnValue = theResultSet.getString(cnt);

					// The value of the column might be NULL. In that case, we DON'T
					// put it into the Properties object
					if (theColumnValue != null)
					{
						thisRow.setProperty(theColumnName, theColumnValue);
					}
				}
				resultSetToReturn.addElement(thisRow);
			}

			if (theResultSet != null)
				theResultSet.close();	
			return resultSetToReturn;
		}
		catch (SQLException sqle)
		{
//			DEBUG: 
			System.err.println( "An SQL Error Occured:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "getQueriedState", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			return null;
		}
		finally
		{
			
			closeStatement();
		}
	}

 	/**
     * Execute the SQL SELECT statement specified by the String parameter to extract 
     * the required fields from the database.
     * Returns a Vector with each element being a Properties object
     * containing the columnName=columnValue mappings
     */
    //------------------------------------------------------------
    protected Vector getSelectQueryResult(String sqlSelectStatement)
    {
		int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet
		ResultSet theResultSet = null;			// the resultset from the SQLStatement execution

		try
		{
			// connect to the database
			//new Event(Event.getLeafLevelClassName(this), "getPersistentState", "Attempting to get database connection", Event.DEBUG);

			theDBConnection = myBroker.getConnection();

			//new Event(Event.getLeafLevelClassName(this), "getPersistentState", "Return from database connection attempt call",
			//	Event.DEBUG);

			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.getSelectQueryResult - Could not connect to database!");
				return null;
			}

			// verify the construction (should be exception?)
			if ((sqlSelectStatement == null) || (sqlSelectStatement.length() == 0))
			{
				System.err.println("Persistable.getSelectQueryResult - input SQL Select Statement Missing!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(MAX_ROWS);

			// The method executeQuery executes a query on the database. The
			// return result is of type ResultSet which is one or more rows in
			// this case.
			theResultSet = theStatement.executeQuery(sqlSelectStatement);
			// verify the results
			if (theResultSet == null)
			{
				System.err.println("Persistable.getSelectQueryResult - Invalid result set from SQL statement!");
				return null;
			}

			// get the column information from the ResultSet
			ResultSetMetaData rsMetaData = theResultSet.getMetaData();

			numRSColumns = rsMetaData.getColumnCount();
			namesRSColumns = new Vector();
			for (int cnt = 1; cnt <= numRSColumns; cnt++)
			{
				String thisColumnName = rsMetaData.getColumnName(cnt);
				namesRSColumns.addElement(thisColumnName);
			}

			Vector resultSetToReturn = new Vector();

			while (theResultSet.next() == true)
			{
				Properties thisRow = new Properties();
				for (int cnt = 1; cnt <= numRSColumns; cnt++)
				{
					String theColumnName = (String)namesRSColumns.elementAt(cnt-1);
					String theColumnValue = theResultSet.getString(cnt);

					// The value of the column might be NULL. In that case, we DON'T
					// put it into the Properties object
					if (theColumnValue != null)
					{
						thisRow.setProperty(theColumnName, theColumnValue);
					}
				}
				resultSetToReturn.addElement(thisRow);
			}			

			if (theResultSet != null)
				theResultSet.close();	
			return resultSetToReturn;
		}
		catch (SQLException sqle)
		{
//			DEBUG: System.err.println( "An SQL Error Occurred:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			
			new Event(Event.getLeafLevelClassName(this), "getSelectQueryResult", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			return null;
		}
		finally
		{
			closeStatement();
		}
	}


    /**
     * Create and execute a SQL statement to extract the required
     * fields from the database.
     * Returns an int indicating the return code from SQL UPDATE statement
     */
    //------------------------------------------------------------
    protected Integer updatePersistentState(Properties schema, 			// the table schema
    						  			    Properties updateValues,	// the values to update
							  			    Properties whereValues) 	// the where values
			throws SQLException
    {

		int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet

		try
		{
			System.out.println("Connecting to database");
			// connect to the database
			theDBConnection = myBroker.getConnection();
			// verify the connection
			if (theDBConnection == null)
			{
				System.out.println("Can't connect");
				System.err.println("Persistable.updatePersistentState - Could not connect to database!");
				return null;
			}

	    	// construct a SQL statement from the passed parameters
			SQLUpdateStatement theSQLStatement = new SQLUpdateStatement(schema, updateValues, whereValues);
			// DEBUG System.out.println("SQL Statement: " + theSQLStatement.toString());

			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.out.println("Sql statement null");
				System.err.println("Persistable.updatePersistentState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(20000);


			// The method executeUpdate executes a query on the database. The
			// return result is of type integer which indicates the number of rows updated
			int returnCode = theStatement.executeUpdate(theSQLStatement.toString());

			// DEBUG: throw new SQLException("Testing only");

			

			return new Integer(returnCode);
		}
		catch (SQLException sqle)
		{
//			DEBUG: 
			System.err.println( "An SQL Error Occured:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "updatePersistentState", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			throw sqle;
		}
		finally
		{
			closeStatement();
		}
	}


   /**
     * Create and execute a SQL statement to insert the required
     * fields into the database.
     * Returns an int indicating the auto-incremental id from SQL INSERT statement
     */
 //------------------------------------------------------------
	protected Integer insertAutoIncrementalPersistentState(Properties schema, 		// the table schema
														   Properties insertValues)	// the values to update
				throws SQLException
    {
		int autoIncKey = -1; 			// auto-increment key extracted from ResultSet
		ResultSet theResultSet = null;	// auto-increment key in ResultSet

		try
		{
			// connect to the database
			theDBConnection = myBroker.getConnection();
			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.insertPersistentState - Could not connect to database!");
				return null;
			}

			// construct a SQL statement from the passed parameters
			SQLInsertStatement theSQLStatement = new SQLInsertStatement(schema, insertValues);
			// DEBUG System.out.println("Persistable.insertPersistentState - SQL Statement: " + theSQLStatement.toString());

			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.err.println("Persistable.insertPersistentState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(20000);

			// The method executeUpdate executes a query on the database. The
			// return result is of type integer which indicates the number of rows updated
			int numRows = theStatement.executeUpdate(theSQLStatement.toString(), Statement.RETURN_GENERATED_KEYS);

			// DEBUG: throw new SQLException("Testing only");
			
			// DEBUG: System.out.println("Testing only");
			
			theResultSet = theStatement.getGeneratedKeys();
			
			if (theResultSet.next()) 
			{
        			autoIncKey = theResultSet.getInt(1);
    		} 
    		else 
    		{
				System.out.println("Persistable.insertAutoIncrementalPersistentState - can't get the auto-increment key");
			}
			
			return new Integer(autoIncKey);
		}
		catch (SQLException sqle)
		{
//			DEBUG: System.err.println( "Persistable.insertAutoIncrementalPersistentState: An SQL Error Occurred: SQL State: " + sqle.getSQLState() + "; Error Code = " + sqle.getErrorCode() + "; Message: " + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "insertAutoIncrementalPersistentState", "SQL Exception: " 
			+ sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR); 
			//return new Integer(28);
			throw sqle;
			
		}
		finally
		{
			if (theResultSet != null)
				theResultSet.close();	
			closeStatement();
		}
	}				
				
				
    /**
     * Create and execute a SQL statement to insert the required
     * fields into the database.
     * Returns an int indicating the return code from SQL INSERT statement
     */
    //------------------------------------------------------------
    protected Integer insertPersistentState(Properties schema, 			// the table schema
    						  			    Properties insertValues)	// the values to update
    			throws SQLException

    {
		int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet

		try
		{
			// connect to the database
			theDBConnection = myBroker.getConnection();
			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.insertPersistentState - Could not connect to database!");
				return null;
			}

	    	// construct a SQL statement from the passed parameters
			SQLInsertStatement theSQLStatement = new SQLInsertStatement(schema, insertValues);
			// DEBUG System.out.println("Persistable.insertPersistentState - SQL Statement: " + theSQLStatement.toString());
		
			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.err.println("Persistable.insertPersistentState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(20000);

			// The method executeUpdate executes a query on the database. The
			// return result is of type integer which indicates the number of rows updated
			int returnCode = theStatement.executeUpdate(theSQLStatement.toString());

			// DEBUG: throw new SQLException("Testing only");

			 return new Integer(returnCode);
		}
		catch (SQLException sqle)
		{
//			DEBUG: 
			System.err.println( "An SQL Error Occurred:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "insertPersistentState", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			throw sqle;
		}
		finally
		{
			closeStatement();
		}
	}


 	/**
     * Create and execture a SQL statement to delete the required
     * fields from the database.
     * Returns an integer indicating the return code from SQL DELETE statement
     */
    //------------------------------------------------------------
    protected Integer deletePersistentState(Properties schema,			// the table schema
    										Properties whereValues)		// the values to use to identify the delete row
    			throws SQLException
    {
    	int numRSColumns = 0; 			// number of columns in ResultSet
		Vector namesRSColumns = null;	// names of columns in ResultSet

		try
		{
			// connect to the database
			theDBConnection = myBroker.getConnection();
			// verify the connection
			if (theDBConnection == null)
			{
				System.err.println("Persistable.deletePersistentState - Could not connect to database!");
				return null;
			}

	    	// construct a SQL statement from the passed parameters
			SQLDeleteStatement theSQLStatement = new SQLDeleteStatement(schema, whereValues);
			// DEBUG System.out.println("Persistable.deletePersistentState - SQL Statement: " + theSQLStatement.toString());

			// verify the construction (should be exception?)
			if(theSQLStatement == null)
			{
				System.err.println("Persistable.deletePersistentState - Could not create SQL Statement!");
				return null;
			}

			// Once a connection has been established we can create an instance
			// of Statement, through which we will send queries to the database.
			// Only the Global Pool connection should be used!
			Statement theStatement = theDBConnection.createStatement();

			// Stop Runaway Queries
			theStatement.setMaxRows(20000);

			// The method executeQuery executes a query on the database. The
			// return result is of type integer which indicates the number of rows updated
			int returnCode = theStatement.executeUpdate(theSQLStatement.toString());

			// DEBUG: throw new SQLException("Testing only");

			 
			 return new Integer(returnCode);
		}
		catch (SQLException sqle)
		{
//			DEBUG: 
			System.err.println( "An SQL Error Occured:" + sqle + "\n" + sqle.getErrorCode() + "\n" + sqle.getMessage() + "\n" + sqle);
			new Event(Event.getLeafLevelClassName(this), "deletePersistentState", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
			throw sqle;
		}
		finally
		{
			closeStatement();
		}
    }

	// close the opened statement on the database
    //------------------------------------------------------------
    private void closeStatement()
    {
		try
		{
			if (theStatement != null)
			{
				theStatement.close();
				theStatement = null;
			}
		}
		catch (SQLException sqle)
		{
			new Event(Event.getLeafLevelClassName(this), "closeStatement", "SQL Exception: " + sqle.getErrorCode() + ": " + sqle.getMessage(), Event.ERROR);
		}
    }

}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: Persistable.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.19  2005/02/07 15:57:32  smitra
//	Put in debug messages via event.Event
//
//	Revision 1.18  2004/03/01 01:11:42  smitra
//	No changes - just debug on/debug off
//
//	Revision 1.17  2004/01/28 05:56:16  smitra
//	Put in persistableStatus state attribute
//
//	Revision 1.16  2004/01/23 18:09:28  smitra
//	Make the db update methods throw an SQL exception on db error
//
//	Revision 1.15  2003/10/28 06:09:37  smitra
//	Changed srcSchema to selSchema and destSchema to projectionSchema
//
//	Revision 1.14  2003/10/26 18:36:09  tomb
//	Added destination schema to limit the number of fields returned by a query.
//
//	Revision 1.13  2003/10/26 00:33:13  tomb
//	Removed debug statement.
//
//	Revision 1.12  2003/10/22 00:28:54  tomb
//	Added getQueryState method for queries.
//
//	Revision 1.11  2003/10/08 17:17:59  tomb
//	Added insertPersistentState method.
//
//	Revision 1.10  2003/10/07 20:35:11  tomb
//	Reformatted slightly.
//
//	Revision 1.9  2003/10/05 14:54:40  tomb
//	Added a few comments, added setting type of field to "text" or "numeric".
//
//	Revision 1.8  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//
//	Revision 1.7  2003/10/03 20:03:54  tomb
//	Switched interface of getPersistentState to take schema and where clause.
//
//	Revision 1.6  2003/10/02 01:37:27  tomb
//	Commented out debug statement.
//
//	Revision 1.5  2003/10/01 03:37:51  smitra
//	Fixed the way in which updatePersistentState invoked JDBC
//
//	Revision 1.4  2003/10/01 01:20:32  tomb
//	Changed names, added update state method.
//
//	Revision 1.3  2003/09/14 23:32:34  tomb
//	Changed Event method invocations to lower case.
//
//	Revision 1.2  2003/09/07 21:14:37  tomb
//	Split SQLStatement into a new class.
//
//	Revision 1.1  2003/09/05 16:35:24  tomb
//	Combined DatabaseAccessor and DatabaseManipulator into this base class.
//
