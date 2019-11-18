/**
 * 
 */
package net.paramount.service.mailing;

import java.nio.charset.Charset;

/**
 * @author bqduc
 *
 */
public interface MailConstants {
	final static String MAIL_TEMPLATE_ENCODING = Charset.forName("UTF-8").name();
	final static String MAIL_TEMPLATE_FREE_MARKER = "/templates/email";
	final static String MAIL_TEMPLATE_PREFIX = "/mail/";
}
