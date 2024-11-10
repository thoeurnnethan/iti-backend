package com.iti.thesis.helicopter.thesis.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import com.iti.thesis.helicopter.thesis.core.collection.MData;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MStringUtil extends org.springframework.util.StringUtils{
	
	public final String	LINE_SEPERATOR				= "\n";
	public final String	DOT_SEPERATOR				= ".";
	public final String	NULL_CHARACTER_UPP_STRING	= "NULL";
	public final String	NULL_CHARACTER_LOW_STRING	= "null";
	public final String	TRUE_STRING					= "true";
	public final String	FALSE_STRING				= "false";
	public final String	ZERO						= "0";
	public static final String	EMPTY				= "";
	public final String	SPACE						= " ";
	
	/**
	 * <pre>
	 *	Removes control characters (char <= 32) to starts of this String
	 * </pre>
	 */
	public String ltrim(String s) {
		return s.replaceAll("^\\s+", EMPTY);
	}
	
	/**
	 * <pre>
	 *	Removes control characters (char <= 32) from ends of this String
	 * </pre>
	 */
	public String rtrim(String s) {
		return s.replaceAll("\\s+$", EMPTY);
	}
	
	/**
	 * <pre>
	 *	Removes control characters (char <= 32) from both ends of this String, handling null by returning null.
	 * </pre>
	 */
	public String trim(String s) {
		return StringUtil.trim(s);
	}
	
	/**
	 * <pre>
	 *	Checks if the string contains only digits
	 * </pre>
	 */
	public boolean isDigit(String digit) {
		
		try {
			BigDecimal n = new BigDecimal(digit);
			
			if (n.toString().length() > 0) {
				return true;
			} else {
				return false;
			}
			
		} catch (NumberFormatException ex) {
			return false;
		}
		
	}
	
	/**
	 * <pre>
	 *	Checks if the string is blank or null
	 * </pre>
	 */
	public static boolean isEmpty(String s) {
		return StringUtil.isEmpty(s);
	}
	
	/**
	 * <pre>
	 *	Left pad a String with a specified String.
	 * </pre>
	 */
	public String leftPad(String str, int size, String padStr) {
		return StringUtils.leftPad(str, size, padStr);
	}
	
	/**
	 * <pre>
	 *	Right pad a String with a specified String.
	 * </pre>
	 */
	public String rightPad(String str, int size, String padStr) {
		return StringUtils.rightPad(str, size, padStr);
	}
	
	/**
	 * <pre>
	 *	This String returning an empty String ("") if the String is {@code null}
	 * </pre>
	 */
	public String nullToEmpty(String str) {
		return str == null ? EMPTY : str;
	}
	
	/**
	 * <pre>
	 *	This String returning the input value if the String is {@code null}
	 * </pre>
	 */
	public String nullToValue(String str, String value) {
		return str == null ? value : str;
	}
	
	/**
	 * <pre>
	 *	This String returning the defaultValue if the value is {@code Empty}
	 * </pre>
	 */
	public String defaultIfEmpty(String value, String defaultValue) {
		return isEmpty(value) ? defaultValue : value;
	}
	
	/**
	 * <pre>
	 *	Gets a substring from the specified String avoiding exceptions.
	 * </pre>
	 */
	public String substring(String str, int start) {
		return StringUtils.substring(str, start);
	}
	
	/**
	 * <pre>
	 *	Gets a substring from the specified String avoiding exceptions.
	 * </pre>
	 */
	public String substring(String str, int start, int end) {
		return StringUtils.substring(str, start, end);
	}
	
	/**
	 * <pre>
	 *	Converts a String to upper case as per String.toUpperCase().
	 * </pre>
	 */
	public String upperCase(String str) {
		return StringUtils.upperCase(str);
	}
	
	/**
	 * <pre>
	 *	Converts a String to lower case as per String.toLowerCase().
	 * </pre>
	 */
	public String lowerCase(String str) {
		return StringUtils.lowerCase(str);
	}
	
	/**
	 * <pre>
	 *	Encode the input value as Base64.
	 * </pre>
	 */
	public String encodeBase64ByString(String text) {
		byte[]	bytes				= text.getBytes();
		String	encodedBase64string	= Base64.encodeBase64String(bytes);
		return encodedBase64string;
	}
	
	/**
	 * <pre>
	 *	Fill the right with the character given in the string
	 * </pre>
	 */
	public String fillRight(String str, byte chr, int len) {
		
		if (str == null) {
			str = EMPTY;
		}
		
		byte[] ss;
		
		try {
			ss = str.getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			return str;
		}
		
		if (len <= ss.length) {
			return str;
		}
		
		byte[]	chs	= new byte[len];
		int		j	= 0;
		
		for (int inx = 0; inx < ss.length; inx++ ) {
			chs[j++ ] = ss[inx];
		}
		
		for (; j < len; j++ ) {
			chs[j] = chr;
		}
		
		try {
			return new String(chs, StandardCharsets.UTF_8);
		} catch (Exception e) {
			return str;
		}
		
	}
	
	/**
	 * <pre>
	 *	string character (true, false) to boolean type cast
	 * </pre>
	 */
	public boolean stringToBoolean(String targetString) {
		boolean flag = false;
		
		if (((trim(lowerCase(targetString)).equalsIgnoreCase(TRUE_STRING)) || (trim(lowerCase(targetString)).equalsIgnoreCase(FALSE_STRING)))
				&& trim(lowerCase(targetString)).equalsIgnoreCase(TRUE_STRING)) {
			flag = true;
		}
		
		return flag;
	}
	
	public String cropByte(String str, int idx, int length) {
		
		if (str == null) {
			return EMPTY;
		}
		
		String	tmp		= str;
		int		slen	= 0, blen = 0, islen = 0, iblen = 0;
		char	c;
		
		if (tmp.getBytes().length < idx + length) {
			length = tmp.getBytes().length - idx;
		}
		
		while (blen < idx + length) {
			c = tmp.charAt(slen);
			
			if (iblen < idx) {
				iblen++ ;
				islen++ ;
				
				if (c > 0x007F) {
					iblen++ ;
				}
				
			}
			
			blen++ ;
			slen++ ;
			
			if (c > 0x007F) {
				blen++ ;
			}
			
		}
		
		String ret = tmp.substring(islen, slen);
		
		if (ret.getBytes().length > length) {
			ret = alignLeftWithSpace(tmp.substring(islen, slen - 1), length, false);
		}
		
		return ret;
	}
	
	public int getByteLength(String inputText) {
		return inputText.getBytes().length;
	}
	
	public int getByteLength(String inputText, String characterSet) {
		
		try {
			return inputText.getBytes(characterSet).length;
		} catch (UnsupportedEncodingException e) {
			return inputText.getBytes().length;
		}
		
	}
	
	public String getByteString(String inputText, int startIndex, int bytes) {
		return getByteString(inputText, startIndex, bytes, null);
	}
	
	public String getByteString(String inputText, int startIndex, int bytes, String characterSet) {
		String result = EMPTY;
		
		try {
			result = new String(inputText.getBytes(characterSet), startIndex, bytes);
		} catch (UnsupportedEncodingException e) {
			result = new String(inputText.getBytes(), startIndex, bytes);
		}
		
		return result;
	}
	
	public String numberFormat(BigDecimal number) {
		return numberFormat(number, "###,###,##0.00");
	}
	
	public String numberFormat(BigDecimal number, String format) {
		DecimalFormat df = new DecimalFormat(format);
		return df.format(number);
	}
	
	/**
	 * <pre>
	 *	Align left with space in the string
	 * </pre>
	 */
	public String alignLeftWithSpace(String str, int width, boolean isPadSpace) {
		int	diff		= 0;
		int	strLength	= 0;
		
		if (str == null) {
			strLength	= 0;
			str			= EMPTY;
		} else {
			strLength = str.getBytes().length;
		}
		
		if (strLength < width) {
			
			if (isPadSpace) {
				diff = width - strLength;
				char[] blanks = new char[diff];
				
				for (int inx = 0; inx < diff; inx++ ) {
					blanks[inx] = ' ';
				}
				
				return str.concat(new String(blanks));
			} else {
				
				if (strLength == 0) {
					return "";
				} else {
					return str;
				}
				
			}
			
		} else if (strLength > width) {
			return new String(str.getBytes(), 0, width);
		} else {
			return str;
		}
		
	}
	
	/**
	 * <pre>
	 *	Checks if the string is null
	 * </pre>
	 */
	public boolean isNull(String s) {
		return (s == null) ? true : false;
	}
	
	/**
	 * <pre>
	 *	Checks if the object is null
	 * </pre>
	 */
	public boolean isNull(Object obj) {
		return (obj == null) ? true : false;
	}
	
	public void NVL(MData input) {
		NVL(input, SPACE);
	}
	
	public void NVL(MData input, String defaultStr) {
		
		if (input != null) {
			
			for (Object entryObject : input.entrySet()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String, Object>	entry	= (Map.Entry<String, Object>)entryObject;
				String						key		= entry.getKey();
				Object						o		= entry.getValue();
				
				if (o != null && o instanceof String && isNone((String)o)) {
					input.setString(key, defaultStr);
				}
				
			}
			
		}
		
	}
	
	public boolean isNone(String value) {
		return value == null || value.length() == 0;
	}
	
	public String getLTRIM(String iString) {
		
		if (iString == null) {
			return null;
		}
		
		String oString = EMPTY;
		
		for (int i = 0; i < iString.length(); i++ ) {
			
			if ((char)0x20 < iString.charAt(i)) {
				oString = iString.substring(i);
				break;
			}
			
		}
		
		return oString;
	}
	
	public String getRTRIM(String iString) {
		
		if (iString == null) {
			return null;
		}
		
		String oString = EMPTY;
		
		for (int i = iString.length(); i > 0; i-- ) {
			
			if ((char)0x20 < iString.charAt(i - 1)) {
				oString = iString.substring(0, i);
				break;
			}
			
		}
		
		return oString;
	}
	
	/**
	 * <pre>
	 *  Cut string by input length
	 * </pre>
	 */
	public String cutStringByByte(String str, int len) {
		
		if (str == null || str.length() == 0) {
			return EMPTY;
		}
		
		byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
		
		if (utf8.length < len) {
			len = utf8.length;
		}
		
		int	n16		= 0;
		int	advance	= 1;
		int	i		= 0;
		
		while (i < len) {
			advance = 1;
			if ((utf8[i] & 0x80) == 0)
				i += 1;
			else if ((utf8[i] & 0xE0) == 0xC0)
				i += 2;
			else if ((utf8[i] & 0xF0) == 0xE0)
				i += 3;
			else {
				i		+= 4;
				advance	= 2;
			}
			if (i <= len)
				n16 += advance;
		}
		
		return str.substring(0, n16);
	}
	
	/**
	 * <pre>
	 *  Cut string by input length
	 * </pre>
	 * 
	 */
	public String cutStringByByte(String str, int offset, int len) {
		
		if (str == null || str.length() == 0) {
			return EMPTY;
		}
		
		byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);
		
		if (utf8.length < len) {
			len = utf8.length;
		}
		
		int	n16		= 0;
		int	advance	= 1;
		int	i		= 0;
		
		while (i < len) {
			advance = 1;
			if ((utf8[i] & 0x80) == 0)
				i += 1;
			else if ((utf8[i] & 0xE0) == 0xC0)
				i += 2;
			else if ((utf8[i] & 0xF0) == 0xE0)
				i += 3;
			else {
				i		+= 4;
				advance	= 2;
			}
			if (i <= len)
				n16 += advance;
		}
		
		return str.substring(offset, n16);
	}
	
}
