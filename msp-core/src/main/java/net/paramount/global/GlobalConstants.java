/**
 * 
 */
package net.paramount.global;

import org.apache.commons.lang.StringUtils;

/**
 * @author bqduc
 *
 */
public interface GlobalConstants {
	final static short	SIZE_STRING_TINY = 50;

	final static byte		SIZE_SERIAL = 15;
	final static byte		SIZE_CODE_MIN = 3;
	final static byte		SIZE_CODE = 20;//Including the backup part
	final static byte		SIZE_CURRENCY_CODE = 5;
	final static byte		SIZE_POSTAL_CODE = 7;
	final static short	SIZE_NAME = 250;
	final static short	SIZE_NAME_MEDIUM = 150;
	final static short	SIZE_NAME_SHORT = 100;
	final static short	SIZE_NAME_TINY = 50;
	final static byte		SIZE_LANGUAGE_CODE = 7;

	final static byte		SIZE_ISBN_10 = 10;
	final static byte		SIZE_ISBN_13 = 13;
	final static byte		SIZE_BARCODE = 25;

	final static short	SIZE_ADDRESS_DEFAULT = 250;
	
	final short	SIZE_COUNTRY = 80;

	final static String SERIAL_PATTERN = StringUtils.repeat("0", GlobalConstants.SIZE_CODE);
}
