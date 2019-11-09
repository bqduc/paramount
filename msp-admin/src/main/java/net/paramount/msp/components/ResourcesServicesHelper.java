/**
 * 
 */
package net.paramount.msp.components;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import net.paramount.exceptions.ResourcesException;
import net.paramount.framework.component.ComponentBase;

/**
 * @author ducbui
 *
 */
@Component
public class ResourcesServicesHelper extends ComponentBase {
	private static final String CLASSPATH = "classpath:/";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4783730564446715179L;

	@Inject
	private ResourceLoader resourceLoader;

	private Resource loadClasspathResource(String resourcePath) {
		Resource resource = this.resourceLoader.getResource(CLASSPATH + resourcePath);
		log.info("Resource is loaded::" + resource);
		return resource;
	}

	public InputStream loadClasspathResourceStream(String resourcePath) throws ResourcesException {
		Resource resource = null;
		InputStream resourceDataStream = null;
		try {
			resource = loadClasspathResource(resourcePath);
			if (null==resource)
				throw new ResourcesException("Unable to get resource from path: " + resourcePath);

			resourceDataStream = resource.getInputStream();
			log.info("Found resource by given path: " + resourcePath);
			/*
			byte[] bdata = FileCopyUtils.copyToByteArray(resourceDataStream);
			String data = new String(bdata, StandardCharsets.UTF_8);
			System.out.println(bdata);
			*/
		} catch (IOException e) {
			throw new ResourcesException(e);
		}
		return resourceDataStream;
	}

	public File loadClasspathResourceFile(String resourcePath) throws ResourcesException {
		Resource resource = null;
		File resourceFile = null;
		try {
			resource = this.loadClasspathResource(resourcePath);
			if (null==resource)
				throw new ResourcesException("Unable to get resource from path: " + resourcePath);

			String realPath =((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(".");
			log.info("Real path: " + realPath);
			log.info("Is exist: " + resource.exists() + ". Is file: " + resource.isFile() + ". " + ". |" + resource.getDescription());
			resourceFile = resource.getFile();
			log.info("Found resource file by given path: " + resourcePath);
		} catch (IOException e) {
			throw new ResourcesException(e);
		}
		return resourceFile;
	}
}
