package com.bellnexxia.iom.utilities.Utility;

import com.vitria.fc.diag.DefaultLogger;
import com.vitria.fc.diag.DiagLogger;
import com.vitria.msg.CommonMessages;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import java.text.ParseException;
import com.bellnexxia.iom.utilities.codeDecode.*;
import com.bellnexxia.iom.utilities.Logger.*;
import com.bellnexxia.iom.utilities.Error.*;

public class DateUtility
{
  private static final int[] WORKINGDAYS= {2,3,4,5,6};  // NGE-5, 2015 - index for working day from Monday to Friday
  
  // Added DEFAULT_DAY_FORMAT for SIP Trunking Rel 2  - April 2010
  public static final String  DEFAULT_DAY_FORMAT   = "yyyy/MM/dd";
  public static final int  LAST_DAY_WEEK   = 6; // added for CR51 - April 7, 2016
  public static final int  DAY_SUN   = 8; // added for CR51 - April 7, 2016
  public static final int  DAY_SAT   = 7; // added for CR51 - April 9, 2016
  /**
   * Generalized time String format - SIP Trunking Rel2 - April 2010
   */
  public static final String ASN1_GENERALIZED_TIME_FORMAT ="yyyyMMddHHmmss";
  
	public DateUtility()
	{

	}
    //==============================================================================================
	/**
	  * This method converts a date type string and format number to the vitria standard.
	 */
	public String reformatPhoneNumber(String phoneNumber){

		char[] newCharArray = phoneNumber.toCharArray();

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < newCharArray.length; i++) {

			if (Character.isDigit(newCharArray[i])){

				buffer.append(newCharArray[i]);
			}
			buffer.toString();
			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Phone Number String is: " + buffer.toString());
		}

		return buffer.toString();
	}
    //==============================================================================================
	public static String ROCSDatetoIOMDate(String inDate){

		//Define Variables
		Date tempDate = new Date();
		String outDate = null;
		SimpleDateFormat inFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat inFormatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		SimpleDateFormat outFormatter1 = new SimpleDateFormat("yyyy/MM/dd");


		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Inside ROCSDatetoIOMDate, Date =  :" + inDate);

		try {
			//Do not format empty string date
			if (inDate.equals("")) {
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("No translation of empty string date");
				return (inDate);
			}

			//In Format is yyyy-MM-dd
			tempDate = inFormatter1.parse(inDate);

			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Translated yyyy-MM-dd to TempDate = :" + tempDate);

		}
		catch (Exception e)
		{
			try 
			{
				//In Format is yyyy-MM-dd hh:mm:ss.S
				tempDate = inFormatter2.parse(inDate);
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("Translated yyyy-MM-dd hh:mm:ss.S to TempDate = :" + tempDate);
			}
			catch (Exception ex) 
			{
				CommonMessages.logGenericTrace("***Exception in ROCSDatetoIOMDate inDate: " + ex);
						throw new RuntimeException();
			}
		}
		
		try 
		{
			//Format outDate to be IOM standard YYYY/MM/DD --- AL
			outDate = outFormatter1.format(tempDate);
		}
		catch (Exception e) {
					CommonMessages.logGenericTrace("***Exception in ROCSDatetoIOMDate outDate:" + e);

		}


		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Exiting ROCSDateToIOMDate, Date =  :" + outDate);

		return(outDate);
	} 
    //==============================================================================================
	public String IOMDateToRemedyDate(String inDate){

		//Define Variables
		String sYear = null;
		String sMonth = null;
		String sDate = null;
		int iYear = 0;
		int iMonth = 0;
		int iDate = 0;

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
		{
			CommonMessages.logGenericTrace("Inside IOMDateToRemedyDate; Date =  :" + inDate);
		}

		if (inDate.length() < 10)
		{
			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			{
				CommonMessages.logGenericTrace("Invalid date string - less than 10 Chars; returning Date as is; value =  :" + inDate);
			}

			return (inDate);
		}

		//Parse input string
		sYear = inDate.substring(0,4);
		sMonth = inDate.substring(5,7);
		sDate = inDate.substring(8,10);

		iYear = Integer.parseInt(sYear);
		iMonth = Integer.parseInt(sMonth);
		iDate = Integer.parseInt(sDate);

		//Adjust year: To Years since 1900

		iYear = iYear - 1900;

		//Adjust Month : January = 0, February = 1, March = 2 ...
		iMonth = iMonth - 1;

		Date tempDate = new Date(iYear, iMonth, iDate);

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Translating Date :" + tempDate);

		//Get the number of Milliseconds since Jan 1, 1970. Convert to seconds
		long secDate = tempDate.getTime() / 1000;

		Long lOutDate = new Long(secDate);

		return (lOutDate.toString());
	}
    //==============================================================================================
	/**
	  * This method converts a date type string and format number to the vitria standard.
	 */
	public String RemedyDateToIOMDate(String inDate){

		//Define Variables
		String tempDate = null;
		String outDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Inside RemedyDateToIOMDate, Date =  :" + inDate);

		try {
			//Change time from seconds since Jan 1, 1970 to milliseconds since Jan 1, 1970
			tempDate = inDate + "000";
			Long lDate = new Long(tempDate);
			Date dDate = new Date(lDate.longValue());
			//Format outDate to be IOM standard YYYY/MM/DD --- AL
			outDate = formatter.format(dDate);
		}
		catch (Exception e) {
			CommonMessages.logGenericTrace("***Exception in RemedyDateToIOMDate:" + e);
		}

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Exiting RemedyDateToIOMDate, Date =  :" + outDate);

		return (outDate);
	}
    //==============================================================================================
	public static String ATOMUSODSDatetoIOMDate(String inDate){

			//Define Variables
			Date tempDate = new Date();
			String outDate = null;
			SimpleDateFormat inFormatter1 = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat inFormatter2 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.S");
			SimpleDateFormat outFormatter1 = new SimpleDateFormat("yyMMdd");

			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Inside ATOMUSODSDatetoIOMDate, Date =  :" + inDate);

			try 
			{
				//Do not format empty string date
				if (inDate.equals("")) 
				{
					if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
						CommonMessages.logGenericTrace("No translation of empty string date");
					return (inDate);
				}

				//In Format is yyyy/MM/dd
				tempDate = inFormatter1.parse(inDate);

				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("Translated yyyy/MM/dd to TempDate = :" + tempDate);
			}
			catch (Exception e) 
			{
				try 
				{
					//In Format is yyyy/MM/dd hh:mm:ss.S
					tempDate = inFormatter2.parse(inDate);
					if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
						CommonMessages.logGenericTrace("Translated yyyy/MM/dd hh:mm:ss.S to TempDate = :" + tempDate);
				}
				catch (Exception ex) 
				{
					CommonMessages.logGenericTrace("***Exception in ATOMUSODSDatetoIOMDate inDate: " + ex);
							throw new RuntimeException();
				}
			}

			try 
			{
				//Format outDate to be ATOMUSODS standard YYMMDD
				outDate = outFormatter1.format(tempDate);
			}
			catch (Exception e) 
			{
			    CommonMessages.logGenericTrace("***Exception in ATOMUSODSDatetoIOMDate outDate:" + e);
			}
			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Exiting ATOMUSODSDatetoIOMDate, Date =  :" + outDate);
			return(outDate);
	}
    //==============================================================================================
	public static String IPortalDatetoIOMDate(String inDate){

		//Define Variables
		Date tempDate = new Date();
		String outDate = null;
		SimpleDateFormat inFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat inFormatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		SimpleDateFormat outFormatter1 = new SimpleDateFormat("yyyy/MM/dd");


		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Inside IPortalDatetoIOMDate, Date =  :" + inDate);

		try 
		{
			//Do not format empty string date
			if (inDate.equals("")) 
			{
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("No translation of empty string date");
				return (inDate);
			}

			//In Format is yyyy-MM-dd
			tempDate = inFormatter1.parse(inDate);

			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Translated yyyy-MM-dd to TempDate = :" + tempDate);

		}
		catch (Exception e) 
		{
			try 
			{
				//In Format is yyyy-MM-dd hh:mm:ss.S
				tempDate = inFormatter2.parse(inDate);
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("Translated yyyy-MM-dd hh:mm:ss.S to TempDate = :" + tempDate);
			}
			catch (Exception ex) 
			{
				CommonMessages.logGenericTrace("***Exception in IPortalDatetoIOMDate inDate: " + ex);
						throw new RuntimeException();
			}
		}

		try 
		{
			//Format outDate to be IOM standard YYYY/MM/DD --- AL
			outDate = outFormatter1.format(tempDate);
		}
		catch (Exception e) 
		{
			CommonMessages.logGenericTrace("***Exception in IPortalDatetoIOMDate outDate:" + e);
		}


		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Exiting IPortalDateToIOMDate, Date =  :" + outDate);

		return(outDate);
	}
    //==============================================================================================
	public int DatesToServiceInterval(String ServiceIntDate, String Date_Create)
	{
		long TempMillis = 0;
		long TempServiceIntDate = 0;
		long TempDate_Create = 0;
		int ServiceInterval;
		TempServiceIntDate = IOMDateToDateInMillisLong(ServiceIntDate);
		TempDate_Create = IOMDateToDateInMillisLong(Date_Create);
		TempMillis = TempServiceIntDate - TempDate_Create;

		ServiceInterval = (int)(MillisInDays(TempMillis));
		return ServiceInterval;
	}
    //==============================================================================================
	public String ServiceIntervalToDates(String Date_Create, int ServiceInterval)
	{
		long TempMillis = 0;
		long TempDate_Create = 0;
		long TempServiceIntervalDate = 0;
		String ServiceIntDate ="";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

		TempDate_Create = IOMDateToDateInMillisLong(Date_Create);
		TempMillis = DaysInMillis(ServiceInterval);
		TempServiceIntervalDate = TempDate_Create + TempMillis;
		try 
		{
			Long lDate = new Long(TempServiceIntervalDate);
			Date dDate = new Date(lDate.longValue());
			ServiceIntDate = formatter.format(dDate);
			CommonMessages.logGenericTrace("***ServiceIntervalDate:" + ServiceIntDate);
		}
		catch (Exception e) 
		{
			CommonMessages.logGenericTrace("***Exception in ServiceIntervalToDates:" + e);
		}
		return(ServiceIntDate);
	}
    //==============================================================================================
	public long IOMDateToDateInMillisLong(String inDate){

		//Define Variables
		String sYear = null;
		String sMonth = null;
		String sDate = null;
		int iYear = 0;
		int iMonth = 0;
		int iDate = 0;

		//Parse input string
		sYear = inDate.substring(0,4);
		sMonth = inDate.substring(5,7);
		sDate = inDate.substring(8,10);

		iYear = Integer.parseInt(sYear);
		iMonth = Integer.parseInt(sMonth);
		iDate = Integer.parseInt(sDate);

		//Adjust year: From Years since 1900

		iYear = iYear - 1900;

		//Adjust Month : January = 0, February = 1, March = 2 ...
		iMonth = iMonth - 1;

		Date tempDate = new Date(iYear, iMonth, iDate);

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***IOM Translated Date" + tempDate);

		//Get the number of Milliseconds since Jan 1, 1970
		long millisDate = tempDate.getTime();

		return (millisDate);
	}
    //==============================================================================================
	public static String IOMDateToDateInMillis(String inDate){

		//Define Variables
		String sYear = null;
		String sMonth = null;
		String sDate = null;
		int iYear = 0;
		int iMonth = 0;
		int iDate = 0;

		//Parse input string
		sYear = inDate.substring(0,4);
		sMonth = inDate.substring(5,7);
		sDate = inDate.substring(8,10);

		iYear = Integer.parseInt(sYear);
		iMonth = Integer.parseInt(sMonth);
		iDate = Integer.parseInt(sDate);

		//Adjust year: From Years since 1900 to absolute year

		iYear = iYear - 1900;

		//Adjust Month : January = 0, February = 1, March = 2 ...
		iMonth = iMonth - 1;

		Date tempDate = new Date(iYear, iMonth, iDate);

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***IOM Translated Date" + tempDate);

		//Get the number of Milliseconds since Jan 1, 1970
		long millisDate = tempDate.getTime();

		Long lOutDate = new Long(millisDate);

		return (lOutDate.toString());
	}
    //==============================================================================================
	public static long CurrentDateInMillis() {

		//Date object of current date
		Date newDate = new Date();
		//Pull Year, Month and Day from current date.  Note: to get Gregorian values for the
		//Year you must add 1900 and for the Month you must add 1
		int newYear = newDate.getYear();
		int newMonth = newDate.getMonth();
		int newDay = newDate.getDate();

		//Create temp date object using just the Year, Month and Day
		Date tempDate = new Date(newYear, newMonth, newDay);

		//get the value in milliseconds
		long millisDate = tempDate.getTime();

		return millisDate;
	}
    //==============================================================================================
	// function to remove the time value of a date in millis
	public static long DateInMillisNoTime(long DateMillis){

		//Adjust for time zone from GMT to EST
		DateMillis = DateMillis - 18000000;

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***In DateInMillisNoTime , Date in Millis:" + DateMillis);

		long inDays = MillisInDays(DateMillis);

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***In DateInMillisNoTime , Date in Days:" + inDays);

		long inMillis = DaysInMillis(inDays);

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***In DateInMillisNoTime ,  Output Date in Millis:" + inMillis);

		//Adjust for time zone
		inMillis = inMillis + 18000000;
		return inMillis;
	}
    //==============================================================================================
	//Funtion to get the current date in Millis with time set to 12:00 am
	public static long CurrentDateInMillisNoTime(){
		Calendar Today = Calendar.getInstance();
		long DateMillis = 0;

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***Current Date & Time:" + Today);

		int Year = Today.get(Calendar.YEAR);
		int Month = Today.get(Calendar.MONTH);
		int Date = Today.get(Calendar.DATE);
		int Hour = 0;
		int Minute = 0;
		int Second = 1;

		Today.set(Year,Month,Date,Hour,Minute,Second);

		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***Current Date No Time:" + Today);


		Date TodayDate = Today.getTime();
		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***Current Date No Time:" + TodayDate);

		DateMillis = TodayDate.getTime();
		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("***Current Date No Time in Millis:" + DateMillis);

		return DateMillis;
	}
    //==============================================================================================
	public static String CurrentDateString(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

		Date now = new Date();
		String dateString = formatter.format(now);

		return (dateString);
	}
    //==============================================================================================
	public static String CurrentDateTimeStringIOMFormat(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");

		Date now = new Date();
		String dateString = formatter.format(now);

		return (dateString);
	}
    //==============================================================================================
	public static String CurrentDateStringIOMFormat(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd");

		Date now = new Date();
		String dateString = formatter.format(now);

		return (dateString);
	}
    //==============================================================================================
	// Converts Millis to Days
	public static long MillisInDays(long Millis){
		long totalDays = 0;
		totalDays = (Millis/86400000);
		return totalDays;

	}
    //==============================================================================================
	// Converts Days to Millis
	public static long DaysInMillis(long Days){
		long totalMillis = 0;
		totalMillis = (Days*86400000);
		return totalMillis;
	}
    //==============================================================================================
	public static String RemoveCarriageReturns(String inputString) {


		if (inputString != null) {

			StringBuffer outStringBuf = new StringBuffer();
			StringTokenizer token = new StringTokenizer(inputString, "\r\n");

			/*
			 * Loop through all tokenized strings
			 */
			while (token.hasMoreElements()) {

				// Remove all Carriage returns
				outStringBuf.append(token.nextElement());

			} // while

			return(outStringBuf.toString());

		} // if

		return null;


	} 
    //==============================================================================================
    public static long fmodifyWeekendDate(long days ) {
       // boolean isWeekend=false;
        long timeout=days*84400000L;
        GregorianCalendar theCurrentCalendar = new GregorianCalendar();
        Date currentToDate = theCurrentCalendar.getTime();


        long theCurrentInMillis = currentToDate.getTime();
        long theRequestedDateInMillis=theCurrentInMillis+timeout;
        Date theRequestedDate=new Date(theRequestedDateInMillis);


        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(theRequestedDate);
        //calendar.computeFields();
        int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        if( dayOfWeek == Calendar.SATURDAY ){
			return timeout+(172800000L);
		}
		else  if( dayOfWeek == Calendar.SUNDAY ){
			return timeout+(86400000L);
		}
        else {
            return timeout;
		}
    }
    //==============================================================================================
    public static String convertDateSbToVt(String siebelDate){
	/* This method converts a Siebel Date from the Siebel format (mm/dd/yyyy hh:mm:ss)
	// to the iHub Date format (yyyy/mm/dd)										*/

		if(siebelDate == null){
			return null;
		}
		else if(siebelDate.equals("")){
			return "";
		}
		else{

			StringTokenizer sBTkn = new StringTokenizer(siebelDate, " ");

			String tempDate = sBTkn.nextToken();

			sBTkn = new StringTokenizer(tempDate, "/");

			String sbMonth = sBTkn.nextToken();
			String sbDay = sBTkn.nextToken();
			String sbYear = sBTkn.nextToken();

			StringBuffer dateBf = new StringBuffer();

			dateBf.append(sbYear + "/");
			dateBf.append(sbMonth + "/");
			dateBf.append(sbDay);

			IOMLogger.logDebug("","","Siebel to Vitria Date: " + dateBf.toString());

			return dateBf.toString();
		}

	}//end public static String convertDateSbToVt
    //==============================================================================================
	public static String convertDateVtToSb(String iHubDate){
	/* This method converts a Siebel Date to the Siebel format (mm/dd/yyyy)
	// from the iHub Date format (yyyy/mm/dd)										*/
		try{
			if(iHubDate == null){
				return null;
			}
			else if(iHubDate.equals("")){
				return "";
			}
			else{


				StringTokenizer vtTkn = new StringTokenizer(iHubDate, "/");

				String vtYear = vtTkn.nextToken();
				String vtMonth = vtTkn.nextToken();
				String vtDay = vtTkn.nextToken();
				String sbYear = null;

				StringBuffer dateBf = new StringBuffer();

				sbYear = vtYear; //.substring(2);

				dateBf.append(vtMonth + "/");
				dateBf.append(vtDay + "/");
				dateBf.append(sbYear);
				IOMLogger.logDebug("","","Vitria to Siebel Date: " + dateBf.toString());


				return dateBf.toString();
			}
		}
		catch(Exception e){
			IOMLogger.logError("", e,"Error formatting IOM date : " + iHubDate + " to Siebel Date");
			throw new IHubRuntimeException("Invalid Vitria Date Format, " + iHubDate);
		}



	}//end public static String convertDateVtToSb
    //==============================================================================================
	public static boolean isSiebelDate(String inString){

		//Define Variables
		boolean blOut = false;
		Date tempDate = new Date();
		SimpleDateFormat inFormatter1 = new SimpleDateFormat("mm/dd/yyyy");
		SimpleDateFormat inFormatter2 = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");


		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Inside DateUitility.isSiebelDate, inString =  :" + inString);

		try {
			//Return false, if inString is empty, OR if it is in format of yyyy/mm/dd
			if (inString.equals("")||(inString.indexOf("/",0)==4)) {
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("No translation required");
				return (blOut);
			}

			//In Format is mm/dd/yyyy
			tempDate = inFormatter1.parse(inString);
			blOut = true;

			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Translated mm/dd/yyyy to TempDate = :" + tempDate);

		}
		catch (Exception e) {
			try {
				//In Format is mm/dd/yyyy hh:mm:ss
				tempDate = inFormatter2.parse(inString);
				blOut = true;
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("Translated mm/dd/yyyy hh:mm:ss to TempDate = :" + tempDate);

			}
			catch (Exception ex) {
				return(blOut);
			}
		}


		return(blOut);
	}//booleam isSiebelDate
    //==============================================================================================
	public static boolean isHubDate(String inString){

		//Define Variables
		boolean blOut = false;
		Date tempDate = new Date();
		SimpleDateFormat inFormatter1 = new SimpleDateFormat("yyyy/mm/dd");
		SimpleDateFormat inFormatter2 = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");


		if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
			CommonMessages.logGenericTrace("Inside DateUitility.isHubDate, inString =  :" + inString);

		try {
			//Return false, if inString is empty, OR if it is in format of mm/dd/yyyy
			if (inString.equals("")||(inString.indexOf("/",0)==2)) {
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("No translation required");
				return (blOut);
			}

			//In Format is yyyy/mm/dd
			tempDate = inFormatter1.parse(inString);
			blOut = true;

			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Translated yyyy/mm/dd to TempDate = :" + tempDate);

		}
		catch (Exception e) {
			try {
				//In Format is yyyy/mm/dd hh:mm:ss
				tempDate = inFormatter2.parse(inString);
				blOut = true;
				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("Translated yyyy/mm/dd hh:mm:ss to TempDate = :" + tempDate);

			}
			catch (Exception ex) {
				return(blOut);
			}
		}


		return(blOut);
	}//booleam isHubDate
    //==============================================================================================
	/* 	This method converts to the Siebel DateTime format (mm/dd/yyyy hh:mm:ss)
		from the iHub DateTime format (yyyy/mm/dd hh:mm:ss)
	*/
	public static String convertDateTimeVtToSb(String sHubDateTime){


		if(sHubDateTime == null){
			return null;
		}
		else if(sHubDateTime.equals("")){
			return "";
		}
		else{

			Date tmpDate = new Date();
			SimpleDateFormat IhubFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat SiebelFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			try{
				tmpDate = IhubFormatter.parse(sHubDateTime);
			}
			catch(Exception e){
				throw new IHubRuntimeException("Invalid Vitria DateTime Format, " + sHubDateTime);
			}
			String sSiebelDate = SiebelFormatter.format(tmpDate);

			IOMLogger.logDebug("","","Vitria DateTime is: " + sHubDateTime + ", Siebel DateTime is: " + sSiebelDate);

			return sSiebelDate;
		}

	}//end public static String convertTimeStampVtToSb
    //==============================================================================================
	/* 	This method converts to the iHub DateTime format (yyyy/mm/dd hh:mm:ss)
		from the Siebel DateTime format (mm/dd/yyyy hh:mm:ss)
	*/
	public static String convertDateTimeSbToVt(String sSiebelDateTime){


		if(sSiebelDateTime == null){
			return null;
		}
		else if(sSiebelDateTime.equals("")){
			return "";
		}
		else{

			Date tmpDate = new Date();
			SimpleDateFormat IhubFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat SiebelFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			try{
				tmpDate = SiebelFormatter.parse(sSiebelDateTime);
			}
			catch(Exception e){
				throw new IHubRuntimeException("Invalid Siebel DateTime Format, " + sSiebelDateTime);
			}
			String sIhubDate = IhubFormatter.format(tmpDate);

			IOMLogger.logDebug("","","Siebel DateTime is: " + sSiebelDateTime + ", Vitria DateTime is: " + sIhubDate);

			return sIhubDate;
		}

	}//end public static String convertTimeStampVtToSb
	  
	  // methods added for SIP Trunking Rel 2 - April 2010
	
	   /**
      * This method converts an ASN GeneralizedTime which has a format of
      * yyyyMMddHHmmss to a unix timestamp.
      * @param    String        ASN GeneralizedTime string.
      * @return   The int unix timestamp in milliseconds.
      */
     public static long generalizedTimeToUnixTime(String generalizedTime) throws Exception{
        try{

      	  DateFormat formatter = new SimpleDateFormat(ASN1_GENERALIZED_TIME_FORMAT);
  		    Date javaDate = formatter.parse(generalizedTime);
          long unixTime = (long)(javaDate.getTime());

          return unixTime;
        }
        catch(Exception pe){
          throw pe;
        }
     }
     
     /**
      * This method converts a unix timestamp to an GeneralizedTime
      * which has a format of yyyyMMddHHmmss.
      * @param    unixTime        The unix date field to be formatted.
      * @return   The formatted date field (as a String).
      */
     public static String unixTimeToGeneralizedTime(long unixTime){
    	 Date  javaDate = new Date(unixTime);
         //Prepare Generalized Time format
    	 DateFormat formatter = new SimpleDateFormat(ASN1_GENERALIZED_TIME_FORMAT);
    	 return formatter.format(javaDate);
     }
     
     /**
      * This method converts current date to a ASN GeneralizedTime which has a format of yyyyMMddHHmmss.
      * 
      * @return   The formatted date field (as a String).
      */
	   public static String CurrentDateTimeToFormat(){
		    SimpleDateFormat formatter = new SimpleDateFormat (ASN1_GENERALIZED_TIME_FORMAT);

		    Date now = new Date();
		    String dateString = formatter.format(now);

		    return (dateString);
	   }
	   
	   /**
      * This method converts date to a ASN GeneralizedTime which has a format of yyyyMMddHHmmss.
      * @param    dt   Date
      * @return   The formatted date field (as a String).
      */
	   public static String DateTimeToFormat(Date dt){
		    SimpleDateFormat formatter = new SimpleDateFormat (ASN1_GENERALIZED_TIME_FORMAT);
		    String dateString = null;
        if(dt != null)
		        dateString = formatter.format(dt);

		    return (dateString);
	   }
	   
	   /**
      * This method converts date to default format of yyyy/MM/dd.
      * @param    aDate Date 
      * @return   The formatted date field (as a String).
      */
	   public static String getFormatedDateStr( Date aDate ){
        if( aDate == null ){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DAY_FORMAT);
        return dateFormat.format(aDate);
     }
     /**
      * This method converts next date midnight (12 am) from today's date to milli seconds
      * @param    dateStr Date String in format yyyyMMddHHmmss
      * @return   long in Milliseconds
      */
     public static long NextCurrentDateInMillisNoTime(String dateStr) throws Exception {
        long milliSeconds = 0;
        String dateConv = null;
        if(dateStr == null)
            return milliSeconds;
        try{
            int yr = Integer.parseInt( dateStr.substring(0,4) );
            int mo =  Integer.parseInt( dateStr.substring(4,6) );
            int day =  Integer.parseInt( dateStr.substring(6,8) );
            IOMLogger.logInfo("=======> NextCurrentDateInMillisNoTime date: " + dateStr + " year: " + yr + " month: " + mo + " day: " + day , "");
            
            // convert String to datetime 
            Calendar cal = Calendar.getInstance();
		        cal.set(yr, mo - 1, day, 0, 0, 0);
            Date tempDate = cal.getTime();
            IOMLogger.logInfo("=======> NextCurrentDateInMillisNoTime tempDate: " + tempDate, "");
            // add 1 to date
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(tempDate); 
            cal1.add(Calendar.DATE, 1); 
            Date nextDayDate = cal1.getTime();
            // convert nextDayDate to format yyyyMMddHHmmss 
            String formDate = DateTimeToFormat(nextDayDate);
            IOMLogger.logInfo("=======> NextCurrentDateInMillisNoTime in next day midnight: " + nextDayDate, "");
            // convert formDate in to milli seconds
            if(formDate != null)
                milliSeconds = DateUtility.generalizedTimeToUnixTime(formDate);
		        
           
        }
        catch(Exception e){
            throw e;
        }
        return  milliSeconds;
     }
     
     /**
     * Convert java.util.Date to String. Return null for a null
     * @param strDate <String>
     * @param format <String>     
     * @return String 
    */
    public static Date convertStringToDate(String strDate, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt =null;
        try{
            if(strDate != null)
                dt = sdf.parse(strDate);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return dt;
    }
    
     //==============================================================================================
	public static String ATOMUSODSDatetoIOMDate1(String inDate){

			//Define Variables
			Date tempDate = new Date();
			String outDate = null;
			SimpleDateFormat outFormatter1 = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat inFormatter1 = new SimpleDateFormat("yyMMdd");

			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Inside ATOMUSODSDatetoIOMDate1, Date =  :" + inDate);

			try 
			{
				//Do not format empty string date
				if (inDate.equals("")) 
				{
					if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
						CommonMessages.logGenericTrace("No translation of empty string date");
					return (inDate);
				}

				//In Format is yyMMdd
				tempDate = inFormatter1.parse(inDate);

				if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
					CommonMessages.logGenericTrace("Translated yyMMdd to TempDate = :" + tempDate);
			}
			catch (Exception e) 
			{
				CommonMessages.logGenericTrace("***Exception in ATOMUSODSDatetoIOMDate1 inDate: " + e);
				
			}

			try 
			{
				//Format outDate to be IOM standard YYYY/MM/DD
				outDate = outFormatter1.format(tempDate);
			}
			catch (Exception e) 
			{
			    CommonMessages.logGenericTrace("***Exception in ATOMUSODSDatetoIOMDate1 outDate:" + e);
			}
			if (DefaultLogger.traceLevel_ >= DiagLogger.VERBOSE)
				CommonMessages.logGenericTrace("Exiting ATOMUSODSDatetoIOMDate1, Date =  :" + outDate);
			return(outDate);
	}
    
    
    /* Get working date - first business day after the suppiled date
     * @param date Date
     * @param date Date (first business day after the given date)
     */
    public static Date NextBusinessWorkingDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        int dow = cal.get(Calendar.DAY_OF_WEEK) + 1;
        int i =0;
        boolean isWorkingDayFound = false;
        int businessDayCounter = 0;
        
        while(!isWorkingDayFound){
            if( findBusinessDay(dow)){
                cal.set(Calendar.DAY_OF_WEEK, dow);
                isWorkingDayFound = true;
            }
            businessDayCounter += 1;
            CommonMessages.logGenericTrace("NextBusinessWorkingDate() - working date found businessDayCounter: " + businessDayCounter);
            dow += 1;
            if(dow == 8){// to traverse the business day starting from Monday - day of week = 2 for Monday)
                dow = 1;
                
            }
        }
        // subtract days from the dueduate to get to the prior business day 
        if(isWorkingDayFound)
           cal.add(Calendar.DATE, businessDayCounter); 
        
        return cal.getTime();
    }
    
    /* Get working date - count of business days after the suppiled dayInterval
     * @param date Date
     * @param dayInterval - the number of business days wait interval
     * @return - count of days to get to the business day after expiry of supplied dayInerval
     */
    public static int NextBusinessWorkingDay(Date date, int dayInterval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); 
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        //int dow = dayOfWeek + dayInterval;
        int dow = dayOfWeek;
        int i =0;
        boolean isWorkingDayFound = false;
        int businessDayCounter = dayInterval;
        while(!isWorkingDayFound){
            if(dow == 1){
                dow = DAY_SUN;
            }
            CommonMessages.logGenericTrace("NextBusinessWorkingDay() - working date found businessDayCounter: " + businessDayCounter + " and dow = " + dow + " day of week: " + cal.get(Calendar.DAY_OF_WEEK));
            
            if(dow > LAST_DAY_WEEK && dow <= DAY_SUN){// to traverse the business day excluding weekend and starting from in between Monday (2) to Friday (6) 
                // this will work if dow comes to value either 7 or 8 (sat or sun)
                
                dow -= LAST_DAY_WEEK; 
                // find the business day counter
                businessDayCounter += dow;
                 // need to add logic if current day is saturday or sunday - in that case dow should decrement by 1 for sat and serement by 2 for sunday.
                if(dayOfWeek == Calendar.SATURDAY){
                    businessDayCounter -= 1;
                    dow -= 1;
                }
                else if(dayOfWeek == Calendar.SUNDAY){
                    businessDayCounter -= 2;
                    dow -= 2;
                }
            }
            else if(dow > DAY_SUN){
                // this will work if dow comes to value > 8
                dow -= LAST_DAY_WEEK; 
                
                businessDayCounter += 2; // adding 2 days of weekend
                // need to add logic if current day is saturday or sunday - in that case dow should decrement by 1 for sat and derement by 2 for sunday.
                if(dayOfWeek == Calendar.SATURDAY){
                    businessDayCounter -= 1;
                    dow -= 1;
                }
                else if(dayOfWeek == Calendar.SUNDAY){
                    businessDayCounter -= 2;
                    dow -= 2;
                }
            }
            else{
                dow += dayInterval;
                CommonMessages.logGenericTrace("NextBusinessWorkingDay() - else, businessDayCounter: " + businessDayCounter + " dow=" + dow);
            }
           
            if( findBusinessDay(dow)){
                isWorkingDayFound = true;
                CommonMessages.logGenericTrace("NextBusinessWorkingDay() - isWorkingDayFound is true, businessDayCounter: " + businessDayCounter);
            
            }
            dow += 1;
            
        }
        // subtract days from the dueduate to get to the prior business day 
        if(isWorkingDayFound)
           return businessDayCounter;
        
        return -1;
    }
    
     private static boolean findBusinessDay(int dow){
        boolean isFindBusinessDay = false;
        for(int i=0;i<WORKINGDAYS.length;i++){
            if(dow == WORKINGDAYS[i]){
               isFindBusinessDay = true;
               break;
            }
        }
        return isFindBusinessDay;
    }   

    
    
    //==============================================================================================
}//public class Utility
