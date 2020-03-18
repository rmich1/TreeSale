// specify the package
package Utilities;

// system imports
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// project imports

/** Useful Utilities */
//==============================================================
public class Utilities
{
	//----------------------------------------------------------
	public static String convertToDefaultDateFormat(Date theDate)
	{

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		String valToReturn = formatter.format(theDate);

		return valToReturn;

	}

	//----------------------------------------------------------
	public static String convertDateStringToDefaultDateFormat(String dateStr)
	{

		Date theDate = validateDateString(dateStr);

		if (theDate == null)
		{
			return null;
		}
		else
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			String valToReturn = formatter.format(theDate);

			return valToReturn;
		}
	}

	//----------------------------------------------------------
	protected static Date validateDateString(String str)
	{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

			Date theDate = null;

			try
			{
				theDate = formatter.parse(str);
				return theDate;
			}
			catch (ParseException ex)
			{
				SimpleDateFormat formatter2 =
					new SimpleDateFormat("yyyy-MM-dd");

				try
				{
					theDate = formatter2.parse(str);
					return theDate;
				}
				catch (ParseException ex2)
				{
					SimpleDateFormat formatter3 =
						new SimpleDateFormat("yyyy/MMdd");

					try
					{
						theDate = formatter3.parse(str);
						return theDate;
					}
					catch (ParseException ex3)
					{
						SimpleDateFormat formatter4 =
							new SimpleDateFormat("yyyyMM/dd");

						try
						{
							theDate = formatter4.parse(str);
							return theDate;
						}
						catch (ParseException ex4)
						{
							return null;
						}
					}
				}
			}
	}

	//----------------------------------------------------------
	protected String mapMonthToString(int month)
	{
		if (month == Calendar.JANUARY)
		{
			return "January";
		}
		else
		if (month == Calendar.FEBRUARY)
		{
			return "February";
		}
		else
		if (month == Calendar.MARCH)
		{
			return "March";
		}
		else
		if (month == Calendar.APRIL)
		{
			return "April";
		}
		else
		if (month == Calendar.MAY)
		{
			return "May";
		}
		else
		if (month == Calendar.JUNE)
		{
			return "June";
		}
		else
		if (month == Calendar.JULY)
		{
			return "July";
		}
		else
		if (month == Calendar.AUGUST)
		{
			return "August";
		}
		else
		if (month == Calendar.SEPTEMBER)
		{
			return "September";
		}
		else
		if (month == Calendar.OCTOBER)
		{
			return "October";
		}
		else
		if (month == Calendar.NOVEMBER)
		{
			return "November";
		}
		else
		if (month == Calendar.DECEMBER)
		{
			return "December";
		}
		
		return "";
	}

	//----------------------------------------------------------
	protected int mapMonthNameToIndex(String monthName)
	{
		if (monthName.equals("January") == true)
		{
			return Calendar.JANUARY;
		}
		else
		if (monthName.equals("February") == true)
		{
			return Calendar.FEBRUARY;
		}
		else
		if (monthName.equals("March") == true)
		{
			return Calendar.MARCH;
		}
		else
		if (monthName.equals("April") == true)
		{
			return Calendar.APRIL;
		}
		else
		if (monthName.equals("May") == true)
		{
			return Calendar.MAY;
		}
		else
		if (monthName.equals("June") == true)
		{
			return Calendar.JUNE;
		}
		else
		if (monthName.equals("July") == true)
		{
			return Calendar.JULY;
		}
		else
		if (monthName.equals("August") == true)
		{
			return Calendar.AUGUST;
		}
		else
		if (monthName.equals("September") == true)
		{
			return Calendar.SEPTEMBER;
		}
		else
		if (monthName.equals("October") == true)
		{
			return Calendar.OCTOBER;
		}
		else
		if (monthName.equals("November") == true)
		{
			return Calendar.NOVEMBER;
		}
		else
		if (monthName.equals("December") == true)
		{
			return Calendar.DECEMBER;
		}
		
		return -1;
	}
	
	
	//----------------------------------------------------
   	protected boolean checkProperLetters(String value)
   	{
   		for (int cnt = 0; cnt < value.length(); cnt++)
   		{
   			char ch = value.charAt(cnt);
   			
   			if ((ch >= 'A') && (ch <= 'Z') || (ch >= 'a') && (ch <= 'z'))
   			{
   			}
   			else
   			if ((ch == '-') || (ch == ',') || (ch == '.') || (ch == ' '))
   			{
   			}
   			else
   			{
   				return false;
   			}
   		}
   		
   		return true;
   	}
   	
   	//----------------------------------------------------
   	protected boolean checkProperPhoneNumber(String value)
   	{
   		if ((value == null) || (value.length() < 7))
   		{
   			return false;
   		}
   		
   		for (int cnt = 0; cnt < value.length(); cnt++)
   		{
   			char ch = value.charAt(cnt);
   			
   			if ((ch >= '0') && (ch <= '9'))
   			{
   			}
   			else
   			if ((ch == '-') || (ch == '(') || (ch == ')') || (ch == ' '))
   			{
   			}
   			else
   			{
   				return false;
   			}
   		}
   		
   		return true;
   	}

}

