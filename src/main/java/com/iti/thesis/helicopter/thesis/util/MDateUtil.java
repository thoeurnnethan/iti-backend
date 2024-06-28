package com.iti.thesis.helicopter.thesis.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MDateUtil {

	/**
	 * year format : "yyyy"
	 */
	public final static String FORMAT_YEAR = "yyyy";
	/**
	 * month format : "MM"
	 */
	public final static String FORMAT_MONTH = "MM";
	/**
	 * day format : "dd"
	 */
	public final static String FORMAT_DAY = "dd";
	/**
	 * DATE format : "yyyyMMdd"
	 */
	public final static String FORMAT_DATE = "yyyyMMdd";
	/**
	 * DATETIME format : "yyyyMMddHHmmss"
	 */
	public final static String FORMAT_DATETIME = "yyyyMMddHHmmss";
	/**
	 * DATETIME format : "yyyyMMddHHmmssSSS"
	 */
	public final static String FORMAT_FULL_DATETIME = "yyyyMMddHHmmssSSS";
	/**
	 * TIME format : "HHmm"
	 */
	public final static String FORMAT_TIME_SHORT = "HHmm";
	/**
	 * TIME format : "HHmmss"
	 */
	public final static String FORMAT_TIME = "HHmmss";
	/**
	 * TIME format : "HH:mm:ss"
	 */
	public final static String FORMAT_TIME_TYPE1 = "HH:mm:ss";
	/**
	 * TIMESTAMP format : "HHmmssSSS"
	 */
	public final static String FORMAT_TIMESTAMP = "HHmmssSSS";
	/**
	 * hour format : "HH"
	 */
	public final static String FORMAT_HOUR = "HH";
	/**
	 * minute format : "mm"
	 */
	public final static String FORMAT_MINUTE = "mm";
	/**
	 * second format : "ss"
	 */
	public final static String FORMAT_SECOND = "ss";
	/**
	 * millisecond format : "SSS"
	 */
	public final static String FORMAT_MILLISECOND = "SSS";
	
	/**
	 * DATE format type 1 : "yyyy-MM-dd"
	 */
	public final static String FORMAT_DATE_TYPE1 = "yyyy-MM-dd";
	
	/**
	 * DATE format type 2 : "dd-MMM-yy"
	 */
	public final static String FORMAT_DATE_TYPE2 = "dd-MMM-yy";
	/**
	 * DATE format type 3 : "dd MMM yy"
	 */
	public final static String FORMAT_DATE_TYPE3 = "dd MMM yy";
	/**
	 * DATE format type 3 : "dd-MMM-yy HH:mm a"
	 */
	public final String FORMAT_DATE_TYPE4 = "dd-MMM-yy HH:mm a";
	
	/**
	 * File logging format type 1 : "yyyyMMddaa"
	 */
	public final static String FORMAT_FILE_LOG_TYPE1 = "yyyyMMddaa";
	/**
	 * File logging format type 2 : "yyyyMMddHH"
	 */
	public final static String FORMAT_FILE_LOG_TYPE2 = "yyyyMMddHH";
	/**
	 * TIMESTAMP format : "yyyyMMddHHmm"
	 */
	public final static String FORMAT_TIMESTAMP_TYPE1 = "yyyyMMddHHmm";
	/**
	 * <pre>
	 *  Get current system date
	 * </pre>
	 */
	
	public static Date getDate() {
		return new Date();
	}
	/**
	 * <pre>
	 *  Get date from string type 
	 * </pre>
	 */
	public static Date toDate( String date, String format ) throws MException {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat( format, new Locale( "en", "US" ) );
			return sdf.parse( date );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Get Date Object Convert from String using Default date format
	 * </pre>
	 */
	public static Date getDate( String yyyyMMdd ) throws MException {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
			return sdf.parse( yyyyMMdd );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Returns the year of the entered date
	 * </pre>
	 * Ex)
	 * int year = MRDateUtil.getYear("20180701");
	 * 
	 * [return]
	 * 2018
	 */
	public static int getYear( String date ) throws MException {
		try {
			return Integer.parseInt( date.substring( 0, 4 ) );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Returns the month of the entered date
	 * </pre>
	 * Ex)
	 * int month = MRDateUtil.getMonth("20180701");
	 * 
	 * [return]
	 * 7
	 */
	public static int getMonth( String date ) throws MException {
		try {
			int month = Integer.parseInt( date.substring( 4, 6 ) );
			return month;
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Returns the day of the entered date
	 * </pre>
	 * 
	 * Ex)
	 * int day = MRDateUtil.getDay("20180701");
	 * 
	 * [return]
	 * 1
	 */
	public static int getDay( String date ) throws MException {
		try {
			return Integer.parseInt( date.substring( 6, 8 ) );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Returns the hour of the entered time
	 * </pre>
	 * 
	 * Ex)
	 * int hour = MRDateUtil.getHour("2359");	//or "235959"
	 * HHMM / HHMMSS TYPE
	 * [return]
	 * 23
	 */
	public static int getHour( String time ) throws MException {
		int rtnHour = -1;
		try {
			rtnHour = Integer.parseInt( time.substring( 0, 2 ) );
			return rtnHour;
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Returns the minute of the entered time
	 * </pre>
	 * 
	 * Ex)
	 * int hour = MRDateUtil.getMinute("2359");	//or "235959"
	 * HHMM / HHMMSS TYPE
	 * [return]
	 * 59
	 */
	public static int getMinute ( String time ) throws MException {
		int rtnMinute = -1;
		try {
			rtnMinute = Integer.parseInt( time.substring( 2, 4 ) );
			return rtnMinute;
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Returns the second of the entered time
	 * </pre>
	 * 
	 * Ex)
	 * int hour = MRDateUtil.getMinute("235959");
	 * HHMMSS TYPE
	 * [return]
	 * 59
	 */
	public static int getSecond ( String time ) throws MException {
		int rtnSec = -1;
		try {
			if ( ( StringUtils.trim( time ) ).length() >= 6 ) {
				//hhmmss or hhmmss.sss
				rtnSec = Integer.parseInt( time.substring( 4, 6 ) );
			} else {
				rtnSec = 0;
			}
			return rtnSec;
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Compare the date entered and the middle of the month
	 *  True if less than the middle of the month
	 *  False if greater than the middle of the month
	 * </pre>
	 * Ex)
	 * boolean retult = MRDateUtil.isFirstHalfMonth("20180714");
	 * 
	 * [return]
	 * true
	 */
	public static boolean isFirstHalfMonth( String date ) throws MException {
		return getDay( date ) < getMiddleDayMonth( date );
	}
	
	/**
	 * <pre>
	 *  Retrieves the previous date of the entered date.
	 * </pre>
	 * Ex)
	 * String previousDay = MRDateUtil.getPreviousDay("20170614");
	 * 
	 * [return]
	 * 20170613
	 */
	public static String getPreviousDay( String date ) throws MException {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern( FORMAT_DATE );
			LocalDate localDate = LocalDate.parse( date, formatter );
			LocalDate yesterday = localDate.minusDays( 1 );
			return yesterday.format( formatter );
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Make sure that the date entered is the start of the month.
	 *  True if start date
	 *  False if it is not a start date
	 * </pre>
	 * Ex)
	 * boolean result = MRDateUtil.isFirstOfMonthFirstDay("20180714");
	 * 
	 * [return]
	 * false 
	 */
	public static boolean isFirstOfMonthFirstDay( String accountingDate ) throws MException {
		return getDay( accountingDate ) == 1;
	}
	
	/**
	 * <pre>
	 *  Make sure the entry date is the middle of the month
	 *  True if medium date
	 *  False if it is not a medium date
	 * </pre>
	 * Ex)
	 * boolean result = MRDateUtil.isSecondHalfOfMonthFirstDay("20180615");
	 * 
	 * [return]
	 * true
	 */
	public static boolean isSecondHalfOfMonthFirstDay( String accountingDate ) throws MException {
		return getDay( accountingDate ) == getMiddleDayMonth( accountingDate );
	}
	
	/**
	 * <pre>
	 *  Returns the middle date of the date entered.
	 * </pre>
	 * Ex)
	 * int day = MRDateUtil.getMiddleDayMonth("20170614");
	 * 
	 * [return]
	 * 15
	 */
	public static int getMiddleDayMonth( String date ) throws MException {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern( FORMAT_DATE );
			LocalDate localDate = LocalDate.parse( date, formatter );
			int daysInMonth = localDate.lengthOfMonth();
			int midDay = (int) Math.ceil( ( (double) daysInMonth ) / 2 );
			return midDay;
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 *  Make sure that the date entered is greater than the start of month.
	 *  True if it is greater than the first day of the month
	 *  False if this is the first day of the month
	 * </pre>
	 * Ex)
	 * boolean result = MRDateUtil.isNotNewMonthFirstDay("20180601");
	 * 
	 * [return]
	 * false
	 */
	public static boolean isNotNewMonthFirstDay( String accountingDate ) throws MException {
		return getDay( accountingDate ) > 1;
	}
	
	/**
	 * <pre>
	 * Make sure that the date entered is the first day of the quarter.
	 *  True if this is not the first date of the branch
	 *  False if this is the first date of the branch
	 * </pre>
	 * Ex)
	 * boolean result = MRDateUtil.isNotNewQuaterFirstDay("20180601");
	 * 
	 * [return]
	 * true
	 */
	public static boolean isNotNewQuaterFirstDay( String accountingDate ) throws MException {
		int month = getMonth( accountingDate ), day = getDay( accountingDate );
		if ( month == Calendar.JANUARY && day == 1 || month == Calendar.APRIL && day == 1
				|| month == Calendar.JULY && day == 1 || month == Calendar.OCTOBER && day == 1 ) {
			return false;
		}
		return true;
	}
	
	/**
	 * <pre>
	 * Make sure that the date entered is the first day of the year
	 *  True if this is not the first date of the year
	 *  False if the first day of the year
	 * </pre>
	 * Ex)
	 * boolean result = MRDateUtil.isNotNewYearFirstDay("20180601");
	 * 
	 * [return]
	 * true
	 */
	public static boolean isNotNewYearFirstDay( String accountingDate ) throws MException {
		int month = getMonth( accountingDate ), day = getDay( accountingDate );
		return !( Calendar.JANUARY == month && day == 1 );
	}
	
	/**
	 * <pre>
	 * Make sure that the date entered is the first day of the half year of the year
	 *  True if this is not the first date of the half year of the year
	 *  False if this is the first date of the half year of the year
	 * </pre>
	 * Ex)
	 * boolean retult = MRDateUtil.isNotNewHalfYearFirstDay("20180701");
	 * 
	 * [return]
	 * false
	 */
	public static boolean isNotNewHalfYearFirstDay( String accountingDate ) throws MException {
		int month = getMonth( accountingDate ), day = getDay( accountingDate );
		
		if ( month == Calendar.JANUARY && day == 1 || month == Calendar.JULY && day == 1 ) {
			return false;
		}
		return true;
	}
	
	/**
	 * <pre>
	 *  Get current system date using format
	 * </pre>
	 */
	public static String getCurrentFormatDate( String format ) {
		SimpleDateFormat sdf = new SimpleDateFormat( format, new Locale( "en", "US" ) );
		return sdf.format( Calendar.getInstance().getTime() );
	}

	/**
	 * <pre>
	 *  Get current system date and time as string type (yyyyMMddHHmmss)
	 * </pre>
	 */
	public static String getCurrentDateTime() {
		return getCurrentFormatDate( FORMAT_DATETIME );
	}
	
	/**
	 * <pre>
	 *  Get current system date and time as string type (yyyyMMddHHmmssSSS)
	 * </pre>
	 */
	public static String getCurrentDateTimestamp() {
		return getCurrentFormatDate( FORMAT_FULL_DATETIME );
	}

	/**
	 * <pre>
	 *  Get current system time as string type (HHmmss)
	 * </pre>
	 */
	public static String getCurrentTime() {
		return getCurrentFormatDate( FORMAT_TIME );
	}
	
	/**
	 * <pre>
	 *  Get current system time as string with custom format
	 * </pre>
	 */
	public static String getCurrentTime(String format) {
		return getCurrentFormatDate( format );
	}

	/**
	 * <pre>
	 *  Get current system date as string type (yyyyMMdd)
	 * </pre>
	 */
	public static String getCurrentDate() {
		return getCurrentFormatDate( FORMAT_DATE );
	}

	/**
	 * <pre>
	 *  Get current system date as string type (HHmmssSSS)
	 * </pre>
	 */
	public static String getCurrentTimestamp() {
		return getCurrentFormatDate( FORMAT_TIMESTAMP );
	}

	/**
	 * <pre>
	 *  Get current system date as format
	 * </pre>
	 */
	public static String getCurrentDate( String format ) throws MException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat( format, new Locale( "en", "US" ) );
			return sdf.format( Calendar.getInstance().getTime() );
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}

	/**
	 * <pre>
	 *  Change date from beforeFormat to afterFormat
	 * </pre>
	 */
	public static String changeDateFormat( String date, SimpleDateFormat beforeFormat, SimpleDateFormat afterFormat ) throws MException {
		Date tempDate;
		String changedDate = date;
		try {
			tempDate = beforeFormat.parse( date );
			changedDate = afterFormat.format( tempDate );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		return changedDate;
	}

	/**
	 * <pre>
	 *  Change date from beforeFormat to afterFormat
	 * </pre>
	 */
	public static String changeDateFormat( String date, String beforeFormatString, String afterFormatString ) throws MException {
		try {
			SimpleDateFormat beforeFormat = new SimpleDateFormat( beforeFormatString );
			SimpleDateFormat afterFormat = new SimpleDateFormat( afterFormatString, new Locale( "en", "US" ) );
			return changeDateFormat( date, beforeFormat, afterFormat );

		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}

	/**
	 * <pre>
	 *  Compare the system date with the input date.
	 * </pre>
	 */
	public static boolean isToday( int year, int month, int day ) {
		Calendar today = Calendar.getInstance();
		return ( today.get( Calendar.YEAR ) == year && today.get( Calendar.MONTH ) + 1 == month
				&& today.get( Calendar.DAY_OF_MONTH ) == day );
	}

	/**
	 * <pre>
	 *  Compare the system date with the input date.
	 * </pre>
	 */
	public static boolean isToday( String yyyyMMdd ) throws MException {
		SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
		try {
			Date date = sdf.parse( yyyyMMdd );
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			return isToday( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ) + 1,
					calendar.get( Calendar.DAY_OF_MONTH ) );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}

	/**
	 * <pre>
	 *  Return the count of days of the month.
	 * </pre>
	 */
	public static int getCountDayOfMonth( String yyyyMMdd ) throws MException {
		SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
		Date date;
		int lastDay = 0;
		try {
			date = sdf.parse( yyyyMMdd );
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			lastDay = calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		return lastDay;
	}

	/**
	 * <pre>
	 *  Return the count of days of the year.
	 * </pre>
	 */
	public static int getCountDayOfYear( String yyyyMMdd ) throws MException {
		SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
		Date date;
		int countDays = 0;
		try {
			date = sdf.parse( yyyyMMdd );
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			countDays = calendar.getActualMaximum( Calendar.DAY_OF_YEAR );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		return countDays;
	}

	/**
	 * <pre>
	 * Compare the two dates entered to get the number of days that are different.
	 * </pre>
	 */
	public static int getDiffBetweenDates( String fromDate, String toDate, int mode, String format ) throws MException {
		Long diffDays = 0L;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat( format );
			Date beginDate = formatter.parse( fromDate );
			Date endDate = formatter.parse( toDate );

			long diff = endDate.getTime() - beginDate.getTime();
			diffDays = diff / ( 24 * 60 * 60 * 1000 );

			if ( mode == 2 ) {
				diffDays++;
			}

		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		return diffDays.intValue();
	}
	
	/**
	 * <pre>
	 * Compare the two dates entered to get the number of days that are different.
	 * </pre>
	 */
	public static int getDiffBetweenDates( String fromDate, String toDate, int mode ) throws MException {
		return getDiffBetweenDates( fromDate, toDate, mode, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Compare the two dates entered to get the number of days that are different.
	 * </pre>
	 */
	public static int getDiffBetweenDates( String fromDate, String toDate ) throws MException {
		return getDiffBetweenDates( fromDate, toDate, 1, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Compare the two dates entered to get the number of days that are different.
	 * </pre>
	 */
	public static int getDiffBetweenDates( String fromDate, String toDate, String format ) throws MException {
		return getDiffBetweenDates( fromDate, toDate, 1, format );
	}
	
	/**
	 * <pre>
	 * Make sure you include today's date between the two dates you entered.
	 * </pre>
	 */
	public static boolean isBetweenWithinToday( String fromDate, String toDate ) throws MException {
		return isBetweenWithinToday( fromDate, toDate, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Make sure you include today's date between the two dates you entered.
	 * </pre>
	 */
	public static boolean isBetweenWithinToday( String fromDate, String toDate, String format ) throws MException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat( format );
			Date beginDate = formatter.parse( fromDate );
			Date endDate = formatter.parse( toDate );

			Date todayDate = formatter.parse( getCurrentDate( format ) );

			return ( todayDate.compareTo( beginDate ) >= 0 && todayDate.compareTo( endDate ) <= 0 );

		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is less then or equal.
	 * 
	 * ex1) basedDate &gt;= comparedDate : true
	 * ex2) isLessThanEqualTo( "20180206", "20181231", "YYYYMMDD" )
	 *     [return]
	 *      false
	 * </pre>
	 */
	public static boolean isLessThanEqualTo( String basedDate, String comparedDate, String format ) throws MException {
		boolean result = false;
		try {
			int compare = compareTo( basedDate, comparedDate, format );
			result = ( compare == 0 || compare == 1 ) ? true : false;
		} catch ( Exception e ) {
			throw e;
		}
		return result;
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is less then or equal.
	 * 
	 * ex1) basedDate >= comparedDate : true
	 * ex2) isLessThanEqualTo( "20180206", "20181231" )
	 *     [return]
	 *      false
	 * </pre>
	 */
	public static boolean isLessThanEqualTo( String basedDate, String comparedDate ) throws MException {
		return isLessThanEqualTo( basedDate, comparedDate, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is less then.
	 * 
	 * ex1) basedDate > comparedDate : true
	 * ex2) isLessThanTo( "20180206", "20181231", "YYYYMMDD" )
	 *     [return]
	 *      false
	 * </pre>
	 */
	public static boolean isLessThanTo( String basedDate, String comparedDate, String format ) throws MException {
		boolean result = false;
		try {
			int compare = compareTo( basedDate, comparedDate, format );
			result = ( compare == 1 ) ? true : false;
		} catch ( Exception e ) {
			throw e;
		}
		return result;
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is less then.
	 * 
	 * ex1) basedDate > comparedDate : true
	 * ex2) isLessThanTo( "20180206", "20181231" )
	 *     [return]
	 *      false
	 * </pre>
	 */
	public static boolean isLessThanTo( String basedDate, String comparedDate ) throws MException {
		return isLessThanTo( basedDate, comparedDate, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is greater then or equal.
	 * 
	 * ex1) basedDate <= comparedDate : true
	 * ex2) isGreaterThanEqualTo( "20180206", "20181231", "YYYYMMDD" )
	 *     [return]
	 *      true
	 * </pre>
	 */
	public static boolean isGreaterThanEqualTo( String basedDate, String comparedDate, String format ) throws MException {

		boolean result = false;
		try {
			int compare = compareTo( basedDate, comparedDate, format );
			result = ( compare == 0 || compare == -1 ) ? true : false;
		} catch ( Exception e ) {
			throw e;
		}
		return result;
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is greater then or equal.
	 * 
	 * ex1) basedDate <= comparedDate : true
	 * ex2) isGreaterThanEqualTo( "20180206", "20181231" )
	 *     [return]
	 *      true
	 * </pre>
	 */
	public static boolean isGreaterThanEqualTo( String basedDate, String comparedDate ) throws MException {
		return isGreaterThanEqualTo( basedDate, comparedDate, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is greater then.
	 * 
	 * ex1) basedDate < comparedDate : true
	 * ex2) isGreaterThanTo( "20180206", "20181231", "YYYYMMDD" )
	 *     [return]
	 *      true
	 * </pre>
	 */
	public static boolean isGreaterThanTo( String basedDate, String comparedDate, String format ) throws MException {
		boolean result = false;
		try {
			int compare = compareTo( basedDate, comparedDate, format );
			result = ( compare == -1 ) ? true : false;
		} catch ( Exception e ) {
			throw e;
		}
		return result;
	}
	
	/**
	 * <pre>
	 * Make sure that the compared-date compared with the based-date is greater then.
	 * 
	 * ex1) basedDate < comparedDate : true
	 * ex2) isGreaterThanTo( "20180206", "20181231" )
	 *     [return]
	 *      true
	 * </pre>
	 */
	public static boolean isGreaterThanTo( String basedDate, String comparedDate ) throws MException {
		return isGreaterThanTo( basedDate, comparedDate, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Make sure the end-date compared with the start-date.
	 * </pre>
	 */
	public static int compareTo( String startDate, String endDate ) throws MException {
		return compareTo( startDate, endDate, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Make sure the end-date compared with the start-date.
	 * </pre>
	 */
	public static int compareTo( String startDate, String endDate, String format ) throws MException {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat( format );
			Date dtStart = formatter.parse( startDate );
			Date dtEnd = formatter.parse( endDate );
			
			return dtStart.compareTo( dtEnd );
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	public static int compareTo( Date start, Date end ) {
		return start.compareTo( end );
	}
	
	/**
	 * <pre>
	 * Returns the number of months in the two dates entered.
	 * </pre>
	 */
	public static long getDiffMonthBetweenDates( String fromDate, String toDate, int mode ) throws MException {
		long result = 0;
		
		try {
			
			fromDate = getDateStr( fromDate, FORMAT_DATE );
			toDate = getDateStr( toDate, FORMAT_DATE );
			
			Calendar beginDate = toCalendar( fromDate );
			Calendar closeDate = toCalendar( toDate );
			
			int compareDate = closeDate.compareTo( beginDate );
			
			if ( compareDate < 0 ) {
				throw new Exception();
			} else {
				if ( mode == 2 ) {
					closeDate.add( Calendar.DATE, 1 );
				}
			}
			
			long years	= closeDate.get(Calendar.YEAR) - beginDate.get( Calendar.YEAR );
			long months	= closeDate.get(Calendar.MONTH) - beginDate.get( Calendar.MONTH );
			
			result = (long)( years * 12 + months - 1 );
			
			if( ( beginDate.get(Calendar.DATE) < closeDate.get(Calendar.DATE) ) || ( beginDate.get(Calendar.DATE) == closeDate.get( Calendar.DATE ) ) || ( closeDate.get( Calendar.DATE ) == closeDate.getMaximum( Calendar.DAY_OF_MONTH ) ) )
				result += 1;
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		
		return result;
		
	}
	
	/**
	 * <pre>
	 * Returns the number of months in the two dates entered.
	 * </pre>
	 */
	public static long getDiffMonthBetweenDates( String fromDate, String toDate, int mode, String format ) throws MException {
		long result = 0;
		
		try {
			
			fromDate = getDateStr(fromDate, format);
			toDate = getDateStr(toDate, format);
			
			Calendar beginDate = toCalendar(fromDate);
			Calendar closeDate = toCalendar(toDate);
			
			int compareDate = closeDate.compareTo( beginDate );
			
			if ( compareDate < 0 ) {
				throw new Exception();
			} else {
				if ( mode == 2 ) {
					closeDate.add( Calendar.DATE, 1 );
				}
			}
			
			long years	= closeDate.get(Calendar.YEAR) - beginDate.get( Calendar.YEAR );
			long months	= closeDate.get(Calendar.MONTH) - beginDate.get( Calendar.MONTH );
			
			result = (long)( years * 12 + months - 1 );
			
			if( ( beginDate.get(Calendar.DATE) < closeDate.get(Calendar.DATE) ) || ( beginDate.get(Calendar.DATE) == closeDate.get( Calendar.DATE ) ) || ( closeDate.get( Calendar.DATE ) == closeDate.getMaximum( Calendar.DAY_OF_MONTH ) ) )
				result += 1;
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		
		return result;
		
	}
	
	/**
	 * Transform date as long type to string format
	 */
	public static String getDateStr( long date, String outputFormat ) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( outputFormat, new Locale( "en", "US" ) );
		return simpleDateFormat.format( new Date( date ) );
	}
	
	
	/**
	 * Transform date as date type to string format
	 */
	public static String getDateStr( Date date, String outputFormat ) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( outputFormat, new Locale( "en", "US" ) );
		return simpleDateFormat.format( date );
	}
	
	/**
	 * Transform date as long type to string format
	 */
	public static String getDateStr( String date, String inputFormat ) throws MException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( inputFormat, new Locale( "en", "US" ) );
		SimpleDateFormat baseDateFormat = new SimpleDateFormat( FORMAT_DATE );
		
		Date toDate = new Date();
		
		try {
			
			toDate = simpleDateFormat.parse(date);
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		
		return baseDateFormat.format( toDate );
	}
	
	/**
	 * <pre>
	 * Returns the date on which the entered base date is added or subtracted in hours, minutes, and seconds
	 * </pre>
	 * ex) String result = MRDateUtil.addTime( "20180315052700", 18, 33, 59 );
	 *    [return] 20180316000059
	 */
	public static String addTime( String sDate, long hour , long min , long sec ) {
		DateFormat dateFormat = new SimpleDateFormat( FORMAT_DATETIME );
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		
		try {
			
			date = dateFormat.parse(sDate);
			
			if( !dateFormat.format( date ).equals( sDate ) ) {
				throw new Exception();
			}
			
			cal.setTime(date);
			
			cal.add(Calendar.HOUR, (int)hour);
			cal.add(Calendar.MINUTE, (int)min);
			cal.add(Calendar.SECOND, (int)sec);
			
		} catch( Exception e) {
			
			if(sDate == null) {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
			if( e instanceof ParseException ) {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
			if( !dateFormat.format( date ).equals( sDate ) ) {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		
		return dateFormat.format(cal.getTime());
		
	}
	
	/**
	 * <pre>
	 * Returns the date on which the entered base date is added or subtracted in hours, minutes, and seconds
	 * </pre>
	 * ex) String result = MRDateUtil.addTime( "20180315052700000", 18, 33, 59, "yyyyMMddHHmmssSSS" );
	 *    [return] 20180316000059000
	 */
	public static String addTime( String sDate, long hour , long min , long sec, String dateFormatString ) {
		DateFormat dateFormat = new SimpleDateFormat( dateFormatString );
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		
		try {
			
			date = dateFormat.parse(sDate);
			
			if( !dateFormat.format( date ).equals( sDate ) ) {
				throw new Exception();
			}
			
			cal.setTime(date);
			
			cal.add(Calendar.HOUR, (int)hour);
			cal.add(Calendar.MINUTE, (int)min);
			cal.add(Calendar.SECOND, (int)sec);
			
		} catch( Exception e) {
			
			if(sDate == null) {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
			if( e instanceof ParseException ) {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
			if( !dateFormat.format( date ).equals( sDate ) ) {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		
		return dateFormat.format(cal.getTime());
		
	}
	
	/**
	 * <pre>
	 * Calculate and return the date elapsed in the entered date string
	 * </pre>
	 * 
	 * ex) String result = MRDateUtil.addDay( "20180315", 5 );
	 * 	   [return]
	 *     20180320
	 */
	public static String addDay( String sDate, long day ) throws MException {
		try {
			Calendar cal = toCalendar( sDate );
			cal.add(Calendar.DATE, (int)day);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( FORMAT_DATE );
			return simpleDateFormat.format( cal.getTime() );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Calculate and return the month elapsed in the entered date string
	 * </pre>
	 * 
	 * ex) String result = MRDateUtil.addMonth( "20180315", 5 );
	 * 	   [return]
	 *     20180815
	 */
	public static String addMonth( String sDate, long month ) throws MException {
		try {
			Calendar cal = toCalendar( sDate );
			cal.add( Calendar.MONTH, (int) month );
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( FORMAT_DATE );
			return simpleDateFormat.format( cal.getTime() );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Calculate and return the year elapsed in the entered date string
	 * </pre>
	 * 
	 * ex) String result = MRDateUtil.addYear( "20180315", 5 );
	 * 	   [return]
	 *     20230315
	 */
	public static String addYear( String sDate, long year ) throws MException {
		try {
			Calendar cal = toCalendar( sDate );
			cal.add( Calendar.YEAR, (int) year );
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat( FORMAT_DATE );
			return simpleDateFormat.format( cal.getTime() );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * Returns the input date as a Calendar object.
	 */
	public static Calendar toCalendar( String pDate ) throws MException {
		try {
			Calendar calDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
			Date inputDate = sdf.parse( pDate );
			calDate.setTime( inputDate );
			return calDate;
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * Returns the input date as a Calendar object.
	 */
	public static Calendar toCalendar( String pDate, String format ) throws MException {
		try {
			Calendar calDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat( format );
			Date inputDate = sdf.parse( pDate );
			calDate.setTime( inputDate );
			return calDate;
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * Returns the input date as a Calendar object.
	 * 
	 * @param pDate
	 * 			Date to return ( ex) 19980102 )
	 * @param pTime
	 * 			Time to return ( ex) 130520 )
	 * @param format
	 * 			String Format
	 * @return Calendar
	 * 			Calendar for this date
	 * @throws MException 
	 */
	public static Calendar toCalendar( String pDate, String pTime, String format ) throws MException {
		try {
			Calendar calDate = Calendar.getInstance();
			String date = pDate + pTime;
			SimpleDateFormat sdf = new SimpleDateFormat( format );
			Date inputDate = sdf.parse( date );
			calDate.setTime( inputDate );
			return calDate;
		} catch ( ParseException e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the first day of the specified date.
	 * </pre>
	 * Ex)
	 * int firstDay = MRDateUtil.getFirstDayOfMonth(2018,7);
	 * 
	 * [return]
	 * 1
	 */
	public static int getFirstDayOfMonth( int year, int month ) throws MException {
		Calendar calendar = Calendar.getInstance();
		try {
			
			calendar.set(year, month-1, 1);
			return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the first day of the specified date.
	 * </pre>
	 * Ex)
	 * long firstDay = MRDateUtil.getFirstDayOfMonth(2018L,7L);
	 * 
	 * [return]
	 * 1
	 */
	public static long getFirstDayOfMonth( long year, long month ) throws MException {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set((int)year, (int)month-1, 1);
			return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the first day of the specified date.
	 * </pre>
	 * Ex)
	 * String firstDay = MRDateUtil.getFirstDayOfMonth("201807");
	 * 
	 * [return]
	 * 1
	 */
	public static String getFirstDayOfMonth( String day ) throws MException {
		try {
			
			int year = Integer.parseInt(day.substring(0, 4));
			int month = Integer.parseInt(day.substring(4, 6));
			return Integer.toString(getFirstDayOfMonth(year, month));
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns N months and the first day of the specified date.
	 * </pre>
	 * Ex)
	 * String plusFirstDay = MRDateUtil.getFirstDayOfMonth("20180715", 1);
	 * String minuseFirstDay = MRDateUtil.getFirstDayOfMonth("20180715", -1);
	 * 
	 * [return]
	 * 20180801
	 * 20180601
	 */
	public static String getFirstDayOfMonth( String day , long month) throws MException {
		try {
			
			String sDate = day.substring(0, 6) + "01";
			
			return addMonth(sDate, month);
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Return the first day of the month
	 * </pre> 
	 * Ex)
	 * String lastDate = MRDateUtil.getFirstDay("201807");
	 * 
	 * [return]
	 * "20180701"
	 */
	public static String getFirstDay( String date ) throws MException {
		try {
			String sDate = date.substring( 0, 6 ) + "01";
			
			if ( isValidDate( sDate ) ) {
				return sDate;
			} else {
				throw new MException(CommonErrorCode.DATE.getCode());
			}
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Return the last day of the day
	 * </pre> 
	 * Ex)
	 * String lastDate = MRDateUtil.getLastDay("20180715");
	 * 
	 * [return]
	 * "20180731"
	 */
	public static String getLastDay( String date ) throws MException {
		try {
			String sDate = date.substring( 0, 6 );
			int lastDay = 0;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat( FORMAT_DATE );
			Date dDate = formatter.parse( sDate + "01" );
			cal.setTime( dDate );
			lastDay = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
			return sDate + StringUtils.leftPad( String.valueOf( lastDay ), 2, "0" );
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the last day of the specified date.
	 * </pre> 
	 * Ex)
	 * int lastDate = MRDateUtil.getLastDayOfMonth(2018, 7);
	 * 
	 * [return]
	 * 31
	 */
	public static int getLastDayOfMonth( int year, int month ) throws MException {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set( year, month - 1, 1 );
			return calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the last day of the specified date.
	 * </pre> 
	 * Ex)
	 *  long lastDate = MRDateUtil.getLastDayOfMonth(2018L, 7L);
	 * 
	 * [return]
	 * 31
	 */
	public static long getLastDayOfMonth( long year, long month ) throws MException {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set( (int) year, (int) month - 1, 1 );
			return calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
		} catch ( Exception e ) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the last day of the specified date.
	 * </pre> 
	 * Ex)
	 * String lastDate = MRDateUtil.getLastDayOfMonth("201807");
	 * 
	 * [return]
	 * "31"
	 */
	public static String getLastDayOfMonth( String day ) throws MException {
		try {
			
			int year = Integer.parseInt(day.substring(0, 4));
			int month = Integer.parseInt(day.substring(4, 6));
			return  Integer.toString(getLastDayOfMonth(year, month));
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns N months before and after the specified date.
	 * </pre>
	 * Ex)
	 * String plusFirstDay = MRDateUtil.getLastDayOfMonth("20180715", 1);
	 * String minuseFirstDay = MRDateUtil.getLastDayOfMonth("20180715", -1);
	 * 
	 * [return]
	 * 20180831
	 * 20180630
	 */
	public static String getLastDayOfMonth( String day , long month) throws MException {
		try {
			String sDate = day.substring(0, 6) + "01";
			return getLastDay(addMonth(sDate, month));
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the date of the end of the previous year.
	 * </pre> 
	 * Ex)
	 * String lastDate = MRDateUtil.getLastDayOfYear();
	 * 
	 * [return]
	 * 20171231
	 */
	public static String getLastDayOfYear() throws MException {
		try {
			Calendar cal = Calendar.getInstance();
			
			int year = cal.get(Calendar.YEAR) - 1;
			cal.set( year, 11, 31 );
			
			SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
			
			return sdf.format( cal.getTime() );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Returns the date of the end of the previous year based on the base year.
	 * </pre> 
	 * Ex)
	 * String lastDate = MRDateUtil.getLastDayOfYear("20160514");
	 * 
	 * [return]
	 * 20151231
	 */
	public static String getLastDayOfYear( String date ) throws MException {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat( FORMAT_DATE );
			cal.setTime( formatter.parse( date ) );
			
			int year = cal.get(Calendar.YEAR) - 1;
			cal.set( year, 11, 31 );
			
			SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
			return sdf.format( cal.getTime() );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
	
	/**
	 * <pre>
	 * Verify that the data you entered is correct for the date format
	 * </pre>
	 */
	public static boolean isValidDate( String date ) {
		return isValidDate( date, FORMAT_DATE );
	}
	
	/**
	 * <pre>
	 * Verify that the data you entered is correct for the date format
	 * </pre>
	 */
	public static boolean isValidDate( String date, String format ) {
		boolean result = false;
		
		SimpleDateFormat sdf = new SimpleDateFormat( format, new Locale( "en", "US" ) );
		try {
			Date dDate = sdf.parse( date );
			String getDate = sdf.format( dDate );
			if ( date.equals( getDate) ) {
				result = true;
			}
		} catch (Exception e) {
			//
		}
		return result;
	}
	
	/**
	 * <pre>
	 *  Return the count of days of the year.
	 * </pre>
	 */
	public static int getDiffYearBetweenDate( String fromDate, String toDate ) throws MException {
		int result = 0;
		
		try {
			
			fromDate = getDateStr( fromDate, FORMAT_DATE );
			toDate = getDateStr( toDate, FORMAT_DATE );
			
			Calendar beginDate = toCalendar( fromDate );
			Calendar closeDate = toCalendar( toDate );
			
			int compareDate = closeDate.compareTo( beginDate );
			
			if ( compareDate < 0 ) {
				throw new Exception();
			}
			
			long years	= closeDate.get(Calendar.YEAR) - beginDate.get( Calendar.YEAR );
			
			result = (int)( years );
			
			if( ( beginDate.get(Calendar.DATE) < closeDate.get(Calendar.DATE) ) 
					|| ( beginDate.get(Calendar.DATE) == closeDate.get( Calendar.DATE ) ) 
					|| ( closeDate.get( Calendar.DATE ) == closeDate.getMaximum( Calendar.DAY_OF_MONTH ) ) )
				result += 1;
			
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
		
		return result;
	}
	
	/**
	 * <pre>
	 * Returns the date of the end of the year based on the base year.
	 * </pre> 
	 * Ex)
	 * String lastDate = MRDateUtil.getLastDayOfYear("20160514");
	 * 
	 * [return]
	 * 20161231
	 */
	public static String getLastDayOfYear2( String date ) throws MException {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat( FORMAT_DATE );
			cal.setTime( formatter.parse( date ) );
			
			int year = cal.get(Calendar.YEAR);
			cal.set( year, 11, 31 );
			
			SimpleDateFormat sdf = new SimpleDateFormat( FORMAT_DATE );
			return sdf.format( cal.getTime() );
		} catch (Exception e) {
			throw new MException(CommonErrorCode.DATE.getCode());
		}
	}
}

