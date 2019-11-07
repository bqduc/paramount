/**
 * 
 */
package net.paramount.msp.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import lombok.Builder;

/**
 * @author bqduc
 *
 */
@Builder
public class ClassPathResourceUtility {
	public InputStream getInputStream(String resource) throws IOException {
		return 	new ClassPathResource(resource).getInputStream();
	}

	public File getFile(String resource) throws IOException {
		return 	new ClassPathResource(resource).getFile();
	}
}
