/**
 * 
 */
package net.paramount.global;

/**
 * @author bqduc
 *
 */
public interface GlobalConstants {
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
}
